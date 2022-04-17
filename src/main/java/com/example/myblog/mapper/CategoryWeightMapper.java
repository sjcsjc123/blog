package com.example.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.myblog.domain.CategoryWeight;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryWeightMapper extends BaseMapper<CategoryWeight> {

    @Select("select category from category_weight order by weight desc limit 5")
    List<String> showByWeight();

    @Select("select category from category_weight order by weight desc")
    List<String> show();

}
