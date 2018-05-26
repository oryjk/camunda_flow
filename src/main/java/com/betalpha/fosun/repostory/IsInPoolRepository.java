package com.betalpha.fosun.repostory;


import com.betalpha.fosun.model.DataBaseIsIn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IsInPoolRepository extends JpaRepository<DataBaseIsIn, String> {

}