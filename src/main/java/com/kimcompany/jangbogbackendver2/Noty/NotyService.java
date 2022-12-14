package com.kimcompany.jangbogbackendver2.Noty;

import com.kimcompany.jangbogbackendver2.Aws.SqsService;
import com.kimcompany.jangbogbackendver2.Employee.Dto.NotyEmployeeDto;
import com.kimcompany.jangbogbackendver2.Employee.EmployeeSelectService;
import com.kimcompany.jangbogbackendver2.Employee.EmployeeService;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Product.Dto.TryInsertDto;
import com.kimcompany.jangbogbackendver2.Store.Dto.InsertEmployNotyDto;
import com.kimcompany.jangbogbackendver2.Store.Dto.SelectNotyDto;
import com.kimcompany.jangbogbackendver2.Store.StoreSelectService;
import com.kimcompany.jangbogbackendver2.Text.PropertiesText;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.cantFindStoreMessage;
import static com.kimcompany.jangbogbackendver2.Util.UtilService.ExceptionValue;
import static com.kimcompany.jangbogbackendver2.Util.UtilService.getPrincipalDetails;

@Service
@RequiredArgsConstructor
public class NotyService {
    private final SqsService sqsService;
    private final EmployeeSelectService employeeSelectService;
    private final StoreSelectService storeSelectService;
    @Async
    public void doneInsert(TryInsertDto tryInsertDto,MemberEntity insertUser){
        long storeId = Long.parseLong(tryInsertDto.getId());
        List<NotyEmployeeDto> notyEmployeeDtos = employeeSelectService.selectForNoty(storeId);
        SelectNotyDto selectNotyDto = storeSelectService.selectForNoty(storeId).orElseThrow(()->new IllegalArgumentException(cantFindStoreMessage));
        for(NotyEmployeeDto dto :notyEmployeeDtos){
            notyToInsert(dto, tryInsertDto,selectNotyDto,insertUser );
        }
    }
    private void notyToInsert(NotyEmployeeDto notyEmployee,TryInsertDto tryInsertDto,SelectNotyDto selectNotyDto, MemberEntity insertUser){
        String notyMessage=selectNotyDto.getName()+"("+selectNotyDto.getAddr()+")??????\n ????????? ?????????????????? \n"
                +"??????:"+tryInsertDto.getPrice()
                +"??????:"+tryInsertDto.getName()
                +"?????????:"+tryInsertDto.getOrigin()
                +"????????????:"+insertUser.getFirstName()+insertUser.getLastName()+"("+insertUser.getUserId()+")";
        sqsService.sendSqs(PropertiesText.sqsPhoneEndPoint,"SUPERMAN ??????", notyMessage,notyEmployee.getPhone());
        sqsService.sendSqs(PropertiesText.sqsEmailEndPoint,"SUPERMAN ??????", notyMessage,notyEmployee.getEmail());

    }
    @Async
    public void doneInsert(long userId, long storeId,MemberEntity insertUser){
        InsertEmployNotyDto insertEmployNotyDto=employeeSelectService.selectToInsertEmployeeNoty(storeId,userId);
        try {
            insertEmployNotyDto.setAdminPhone(insertUser.getPhone());
            insertEmployNotyDto.setAdminEmail(insertUser.getEmail());
            notyToInsert(insertEmployNotyDto);
        }catch (Exception e){
            ExceptionValue("", EmployeeService.class);
        }
    }
    private void notyToInsert(InsertEmployNotyDto insertEmployNotyDto){
        String notyMessage=insertEmployNotyDto.getStoreName()+"??????("+insertEmployNotyDto.getAddress()+","+insertEmployNotyDto.getStoreTel()+")??????\n ???????????? ?????????????????????";
        sqsService.sendSqs(PropertiesText.sqsPhoneEndPoint,"SUPERMAN ?????? ?????? ??????", notyMessage,insertEmployNotyDto.getEmployeePhone());
        sqsService.sendSqs(PropertiesText.sqsEmailEndPoint,"SUPERMAN ?????? ?????? ??????", notyMessage,insertEmployNotyDto.getEmployeePhone());
        notyMessage=insertEmployNotyDto.getEmployeeName()+"?????????("+insertEmployNotyDto.getEmployeeUserId()+") ?????? ????????? ?????????????????????";
        sqsService.sendSqs(PropertiesText.sqsPhoneEndPoint,"SUPERMAN ?????? ?????? ??????",notyMessage,insertEmployNotyDto.getAdminPhone());
        sqsService.sendSqs(PropertiesText.sqsEmailEndPoint,"SUPERMAN ?????? ?????? ??????",notyMessage,insertEmployNotyDto.getAdminEmail());

    }
}
