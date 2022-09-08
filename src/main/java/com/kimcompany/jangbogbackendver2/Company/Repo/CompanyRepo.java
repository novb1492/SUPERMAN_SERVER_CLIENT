package com.kimcompany.jangbogbackendver2.Company.Repo;

import com.kimcompany.jangbogbackendver2.Company.Dto.SelectListDto;
import com.kimcompany.jangbogbackendver2.Company.Model.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompanyRepo extends JpaRepository<CompanyEntity,Long> ,CompanyRepoCustom{

    @Query("select c from CompanyEntity c where  c.companyNum=:num and c.commonColumn.state<>:state")
    Optional<CompanyEntity>findByCompanyNum(@Param("num")String num,@Param("state")int deleteState);

    @Query("select c from CompanyEntity c where  c.id=:id and c.commonColumn.state<>:state and c.memberEntity.id=:adminId")
    Optional<CompanyEntity>findByIdAndAdminId(@Param("id")long id,@Param("state")int deleteState,@Param("adminId")long adminId);

    @Query("select new com.kimcompany.jangbogbackendver2.Company.Dto.SelectListDto(c.id,c.companyNum) from CompanyEntity  c where c.memberEntity.id=:adminId and c.commonColumn.state<>:state")
    List<SelectListDto> findByAdminIdForList(@Param("adminId") long adminId, @Param("state") int deleteState);
}
