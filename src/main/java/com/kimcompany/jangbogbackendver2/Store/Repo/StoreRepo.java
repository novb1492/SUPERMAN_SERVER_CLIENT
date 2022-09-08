package com.kimcompany.jangbogbackendver2.Store.Repo;

import com.kimcompany.jangbogbackendver2.Store.Dto.SelectNotyDto;
import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreRepo extends JpaRepository<StoreEntity,Long>,StoreRepoCustom {

    @Query("select new com.kimcompany.jangbogbackendver2.Store.Dto.SelectNotyDto(s.name,s.addressColumn.address) from StoreEntity  s where s.id=:id and s.commonColumn.state<>:state")
    Optional<SelectNotyDto>findByIdForNoty(@Param("id")long id,@Param("state")int deleteState);
}
