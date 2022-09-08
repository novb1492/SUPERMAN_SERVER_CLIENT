package com.kimcompany.jangbogbackendver2.Deliver.Service;

import com.kimcompany.jangbogbackendver2.Deliver.Model.DeliverEntity;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Member.Model.PrincipalDetails;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.kimcompany.jangbogbackendver2.Util.EtcService;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.rmi.ServerError;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliverPositionHandler extends TextWebSocketHandler {
    Map<Long, List<Map<String,Object>>> roomList =new HashMap<>(); //웹소켓 세션을 담아둘 리스트 ---roomListSessions

    private final DeliverService deliverService;
    private final DeliverSelectService deliverSelectService;
    private final EtcService etcService;


    @Override//메세지가오는함수
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JSONObject xAndYAndRoom= UtilService.stringToJson(message.getPayload());
        log.info("배달메세지:{}",xAndYAndRoom.toString());
//        //배달종료 메시지가 있다면 store만 해당됨
        if(xAndYAndRoom.containsKey("state")){
            stataAction(xAndYAndRoom);
        }
    }
    public void stataAction(JSONObject xAndYRoom) {
        String state = xAndYRoom.get("state").toString();
        long deliverId=Long.parseLong(xAndYRoom.get("roomid").toString());
        if(state.equals("done")){
            sendOutAtArr(Long.parseLong(xAndYRoom.get("deliverDetailId").toString()), deliverId);
        }else if(state.equals("cancel")){
            sendOutAtArr(Long.parseLong(xAndYRoom.get("deliverDetailId").toString()),deliverId);
        }else if(state.equals("cancelAll")){
            cancelAll(deliverId);
        }else if(state.equals("start")){
            long storeId = Long.parseLong(xAndYRoom.get("storeId").toString());
            startDeliver(xAndYRoom,deliverId,storeId);
        }
    }

    /**
     * 각 배달완료 아니면 배달취소시
     * 웹소캣 배열에서 내보내는 함수
     * @param deliverDetailId
     * @param deliverId
     */
    private void sendOutAtArr(long deliverDetailId,long deliverId){
        try {
            int index=0;
            for(Map<String,Object>room:roomList.get(deliverId)){
                try {
                    if(room.get("deliverDetailId").toString().equals(Long.toString(deliverDetailId))){
                        log.info("삭제번호:{}",deliverDetailId);
                        List<Map<String, Object>> roomDetails = roomList.get(deliverId);
                        roomDetails.remove(index);
                        roomList.put(deliverId, roomDetails);
                        break;
                    }
                } catch (Exception e) {

                }
                index += 1;
            }

        } catch (NullPointerException e) {


        }
    }
    /**
     * 매장에서 전체 취소시
     * 배달방 삭제
     * @param deliverId
     */
    private void cancelAll(long deliverId){
        roomList.remove(deliverId);
    }

    /**
     * 배달 시작 버튼 클릭시
     * 종업원 위치 전송
     * @param xAndYRoom
     */
    private void startDeliver(JSONObject xAndYRoom,long deliverId,long storeId){
        try {
            for(Map<String,Object>room:roomList.get(deliverId)){
                try {
                    //보내기만 하면됨 n번방 세션 들 다꺼내기
                    WebSocketSession wss = (WebSocketSession) room.get("session");
                    wss.sendMessage(new TextMessage(xAndYRoom.toJSONString()));
                } catch (Exception e) {
                }
            }

        } catch (NullPointerException e) {
            UtilService.ExceptionValue("만들어진 방이 없습니다",DeliverPositionHandler.class);
        }
    }
    @Override//연결이되면 자동으로 작동하는함수
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        /**
         * 점원이 연결시 최소에 배달방있는지 검사 없다면 객체생성
         */
        Map<String,Object>params=UtilService.getQueryMap(session.getUri().getQuery());
        String deliverId = params.get("roomid").toString();
        String role=params.get("role").toString();
        if(role.equals(ROLE_ADMIN)){
            long storeId=Long.parseLong(params.get("storeId").toString());
            confirmAdmin(session,storeId);
            adminAction(Long.parseLong(deliverId),storeId);
        }else{
            String deliverDetailId = params.get("deliverDetailId").toString();
            clientAction(session,Long.parseLong(deliverId),Long.parseLong(deliverDetailId));
        }
    }

    /**
     * 직원이 접속시 방이 존재 하지 않는다면
     * 새 배달방생성
     * @param deliverId
     */
    private void adminAction(long deliverId,long storeId) throws SQLException {
        DeliverEntity deliverEntity = deliverSelectService.selectForDeliver(storeId, deliverId).orElseThrow(() -> new IllegalArgumentException("배달이 완료되었거나 조회 할 수없는 배달입니다"));
        if(deliverEntity.getCommonColumn().getState()== deleteState){
            throw new IllegalArgumentException("조회 할 수 없는 배달입니다");
        }
        if(!roomList.containsKey(deliverId)){
            List<Map<String, Object>> room = new ArrayList<>();
            roomList.put(deliverId, room);
        }
    }

    /**
     * 주문자가 배달조회시 입장
     * @param session
     * @param deliverId
     */
    private void clientAction(WebSocketSession session,long deliverId,long deliverDetailId){
        if(!roomList.containsKey(deliverId)){
            throw new IllegalArgumentException("아직 배달이 시작되지 않았습니다");
        }
        //배달방 정보 가져오기
        List<Map<String, Object>> room = roomList.get(deliverId);
        //현재 접속 웹세션 정보 배열에 추가
        room.add(makeRoomDetail(session, deliverId, deliverDetailId));
        roomList.put(deliverId, room);
    }
    @Override //연결이끊기면 자동으로 작동하는함수
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //권한에 따라
    }

    /**
     * 웹소캣 배달방 입장시 필요 정보 만드는 함수
     * @param session
     * @param deliverId
     * @return
     */
    public Map<String,Object> makeRoomDetail(WebSocketSession session, long deliverId,long deliverDetailId) {
        Map<String,Object>roomDetail=new HashMap<>();
        roomDetail.put("roomNumber", deliverId);
        roomDetail.put("userId",1L);
        roomDetail.put("deliverDetailId",deliverDetailId);
        roomDetail.put("sessionId", session.getId());
        roomDetail.put("session", session);
        return roomDetail;
    }

    /**
     * 웹소켓 세션에서 로그인 정보 추출
     * @param session
     * @return
     */
    private MemberEntity getLoginInfo(WebSocketSession session) {
        AbstractAuthenticationToken principal=(AbstractAuthenticationToken) session.getPrincipal();
        PrincipalDetails  principalDetails=(PrincipalDetails) principal.getPrincipal();
        return principalDetails.getMemberEntity();
    }

    /**
     * 어드민 접근시 해당 매장 소속인지 검사
     * @param session
     * @param storeId
     */
    private void confirmAdmin(WebSocketSession session, long storeId){
        MemberEntity loginInfo = getLoginInfo(session);
        etcService.confirmOwn(storeId,loginInfo.getId(),loginInfo.getRole());
    }
}
