package com.kimcompany.jangbogbackendver2.Employee;

import com.kimcompany.jangbogbackendver2.Aws.SqsService;
import com.kimcompany.jangbogbackendver2.Employee.Dto.TryInsertDto;
import com.kimcompany.jangbogbackendver2.Employee.Model.EmployeeEntity;
import com.kimcompany.jangbogbackendver2.Employee.Repo.EmployeeRepo;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Member.Service.MemberSelectService;
import com.kimcompany.jangbogbackendver2.Noty.NotyService;
import com.kimcompany.jangbogbackendver2.Store.Dto.InsertEmployNotyDto;
import com.kimcompany.jangbogbackendver2.Store.StoreSelectService;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.kimcompany.jangbogbackendver2.Text.PropertiesText;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.*;
import static com.kimcompany.jangbogbackendver2.Util.UtilService.*;

/**
 * 매장에서 직원 관련 서비스입니다
 */
@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepo employeeRepo;
    private final EmployeeSelectService employeeSelectService;
    private final MemberSelectService memberSelectService;
    private final SqsService sqsService;
    private final NotyService notyService;


    public void save(TryInsertDto tryInsertDto){
        long userId =0;
        long storeId=0;
        try{
            userId = Long.parseLong(tryInsertDto.getUserId());
            storeId = Long.parseLong(tryInsertDto.getStoreId());
            confirmExistUser(userId);
            confirmExist(storeId, userId);
        }catch (ClassCastException e){
            ExceptionValue(tryInsertDto.toString(), EmployeeService.class);
            throw new IllegalArgumentException("올바르지 않는 매장직원 혹은 매장입니다");
        }
        EmployeeEntity employeeEntity = TryInsertDto.dtoToEntity(tryInsertDto);
        employeeRepo.save(employeeEntity);
        notyService.doneInsert(userId,storeId,UtilService.getPrincipalDetails().getMemberEntity());
    }
    private void doneInsert(long userId, long storeId){
        InsertEmployNotyDto insertEmployNotyDto=employeeSelectService.selectToInsertEmployeeNoty(storeId,userId);
        try {
            MemberEntity memberEntity = getPrincipalDetails().getMemberEntity();
            insertEmployNotyDto.setAdminPhone(memberEntity.getPhone());
            insertEmployNotyDto.setAdminEmail(memberEntity.getEmail());
            notyToInsert(insertEmployNotyDto);
        }catch (Exception e){
            ExceptionValue("",EmployeeService.class);
        }
    }
    private void notyToInsert(InsertEmployNotyDto insertEmployNotyDto){
        String notyMessage=insertEmployNotyDto.getStoreName()+"에서("+insertEmployNotyDto.getAddress()+","+insertEmployNotyDto.getStoreTel()+")에서\n 직원으로 등록하였습니다";
        sqsService.sendSqs(PropertiesText.sqsPhoneEndPoint,"SUPERMAN 직원 초대 알림", notyMessage,insertEmployNotyDto.getEmployeePhone());
        sqsService.sendSqs(PropertiesText.sqsEmailEndPoint,"SUPERMAN 직원 초대 알림", notyMessage,insertEmployNotyDto.getEmployeePhone());
        notyMessage=insertEmployNotyDto.getEmployeeName()+"님에게("+insertEmployNotyDto.getEmployeeUserId()+") 초대 알림이 전송되었습니다";
        sqsService.sendSqs(PropertiesText.sqsPhoneEndPoint,"SUPERMAN 직원 초대 알림",notyMessage,insertEmployNotyDto.getAdminPhone());
        sqsService.sendSqs(PropertiesText.sqsEmailEndPoint,"SUPERMAN 직원 초대 알림",notyMessage,insertEmployNotyDto.getAdminEmail());

    }
    private void confirmExist(long storeId,long userId){
        if(employeeSelectService.exist(storeId,userId, trueStateNum)){
            throw new IllegalArgumentException("해당 매장에 이미 등록 되어있는 직원입니다");
        }
    }

    private void confirmExistUser(long userId){
        if(!memberSelectService.exist(userId)){
            throw new IllegalArgumentException("존재 하지 않는 멤버 입니다");
        }
    }
}
