package com.kimcompany.jangbogbackendver2.Employee.Repo;

import com.kimcompany.jangbogbackendver2.Employee.Model.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepo extends JpaRepository<EmployeeEntity,Long>,EmployeeCustom {

}
