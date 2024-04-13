package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.entity.VarietyAttribute;
import com.project.pescueshop.repository.inteface.VarietyAttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class VarietyAttributeDAO extends BaseDAO{
    private final VarietyAttributeRepository varietyAttributeRepository;

    public List<VarietyAttribute> getAllExistedAttributeByProductId(String productId, String type) {
        String sql = "SELECT * FROM get_product_variety_attributes(:p_productId, :p_type);";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_productId", productId)
                .addValue("p_type", type);

        return jdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper<>(VarietyAttribute.class));
    }

    public void saveAndFlushAttribute(VarietyAttribute attribute){
        varietyAttributeRepository.saveAndFlush(attribute);
    }

    public List<VarietyAttribute> findAll(){
        return varietyAttributeRepository.findAll();
    }
}
