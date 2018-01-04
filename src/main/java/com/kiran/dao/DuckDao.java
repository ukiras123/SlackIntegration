package com.kiran.dao;

import com.kiran.model.entity.DuckEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Kiran
 * @since 12/18/17
 */
@Component
public interface DuckDao extends CrudRepository<DuckEntity, Long> {

    DuckEntity findByUserName(String userName);

    @Query(value = "select * from users.duck_log where total_duck >= (select MAX(total_duck) from duck_log where total_duck <\n" +
            "(select MAX(total_duck) from duck_log where total_duck <\n" +
            "(select MAX(total_duck) from duck_log ))) order by total_duck desc", nativeQuery = true)
    List<DuckEntity> findByTotalDuck();


}
