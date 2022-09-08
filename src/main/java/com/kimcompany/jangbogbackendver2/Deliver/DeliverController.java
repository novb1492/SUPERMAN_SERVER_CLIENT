package com.kimcompany.jangbogbackendver2.Deliver;

import com.kimcompany.jangbogbackendver2.Deliver.Dto.ChangeDetailDto;
import com.kimcompany.jangbogbackendver2.Deliver.Dto.SearchCondition;
import com.kimcompany.jangbogbackendver2.Deliver.Dto.StartDeliverDto;
import com.kimcompany.jangbogbackendver2.Deliver.Dto.TryInsertDto;
import com.kimcompany.jangbogbackendver2.Deliver.Service.DeliverSelectService;
import com.kimcompany.jangbogbackendver2.Deliver.Service.DeliverService;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.sql.SQLException;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.*;

@RestController
@RequiredArgsConstructor
public class DeliverController {
    private final DeliverService deliverService;
    private final DeliverSelectService deliverSelectService;

    /**
     * 배달방 생성
     * @param tryInsertDto
     * @return
     */
    @RequestMapping(value = "/deliver/save",method = RequestMethod.POST)
    public ResponseEntity<?>save(@Valid @RequestBody TryInsertDto tryInsertDto){
        long id = deliverService.save(tryInsertDto);
        JSONObject response = new JSONObject();
        response.put("message", "배달방 생성에 성공했습니다");
        response.put("roomId", id);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 배달 리스트 조회
     * @param request
     * @param storeId
     * @param state
     * @return
     */
    @RequestMapping(value = "/deliver/list/{storeId}/{state}",method = RequestMethod.GET)
    public ResponseEntity<?>selectForList(HttpServletRequest request,@PathVariable String storeId,@PathVariable String state){
        int page = Integer.parseInt(request.getParameter("page"));
        long storeIdToLong = Long.parseLong(storeId);
        int stateToInt = Integer.parseInt(state);
        SearchCondition searchCondition = SearchCondition.set(page, deliverPageSize, storeIdToLong,stateToInt);
        return ResponseEntity.ok().body(deliverSelectService.selectForList(searchCondition));
    }

    /**
     * 배달 상세 조회
     * @param storeId
     * @param deliverId
     * @return
     */
    @RequestMapping(value = "/deliver/{storeId}/{deliverId}",method = RequestMethod.GET)
    public ResponseEntity<?>selectForDetail(@PathVariable String storeId,@PathVariable String deliverId){
        long storeIdToLong = Long.parseLong(storeId);
        long deliverIdToLong = Long.parseLong(deliverId);
        return ResponseEntity.ok().body(deliverService.selectForDetail(storeIdToLong, deliverIdToLong));
    }

    /**
     * 배달시작/전체취소
     * @param startDeliverDto
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/deliver/state",method = RequestMethod.PUT)
    public ResponseEntity<?>deliverStart(@Valid @RequestBody StartDeliverDto startDeliverDto) throws SQLException {
        int stateToInt=startDeliverDto.getState();
        deliverService.updateDeliverAndDeliverDetailAndOrderState(startDeliverDto);
        JSONObject response = new JSONObject();
        String msg=null;
        /**
         * enum으로 if문 제거가능
         */
        if (stateToInt==deliveringState){
            msg = "배달 시작";
        }else if(stateToInt==deliverCancelState){
            msg="전체 배달취소";
        }
        response.put("message",msg);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 배달완료/해당배달취소
     * @param changeDetailDto
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/deliver-detail/state",method = RequestMethod.PUT)
    public ResponseEntity<?>deliverDetailState(@Valid @RequestBody ChangeDetailDto changeDetailDto) throws SQLException {
        int state = changeDetailDto.getState();
        deliverService.updateDeliverAndDeliverDetailAndOrderState(changeDetailDto);
        JSONObject response = new JSONObject();
        String msg=null;
        /**
         * enum으로 if문 제거가능
         */
        if (state==deliverDoneState){
            msg = changeDetailDto.getDeliverDetailId()+"배달 완료";
        }else if(state==deliverCancelState){
            msg=changeDetailDto.getDeliverDetailId()+"배달취소";
        }
        response.put("message",msg);
        return ResponseEntity.ok().body(response);
    }




}
