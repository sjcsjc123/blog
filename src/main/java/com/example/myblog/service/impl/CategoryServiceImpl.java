package com.example.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myblog.domain.Category;
import com.example.myblog.domain.CategoryWeight;
import com.example.myblog.mapper.CategoryMapper;
import com.example.myblog.mapper.CategoryWeightMapper;
import com.example.myblog.service.CategoryService;
import com.example.myblog.vo.ArticleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final Logger logger =
            LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private CategoryWeightMapper categoryWeightMapper;

    //表明目前只开设了这几个专栏
    public final static List<String> categoryNames = Arrays.asList("springboot",
            "java", "redis","mysql","http","Linux","go","算法","博客","Springboot","设计模式");

    @Override
    public void save(ArticleVo articleVo, String username) {
        for (int i = 0; i < categoryNames.size(); i++) {
            if (articleVo.getTitle().contains(categoryNames.get(i))){
                Category category = new Category();
                category.setArticleId(articleVo.getId());
                category.setCategoryId(i);
                category.setCategoryName(categoryNames.get(i));
                categoryMapper.insert(category);
                //此处可采用redis的incr方法
                logger.info(category+"already insert into table");
                QueryWrapper<CategoryWeight> wrapper = new QueryWrapper<>();
                wrapper.eq("category",categoryNames.get(i));
                CategoryWeight categoryWeight =
                        categoryWeightMapper.selectOne(wrapper);
                if(categoryWeight == null){
                    CategoryWeight categoryWeight1 = new CategoryWeight();
                    categoryWeight1.setCategory(categoryNames.get(i));
                    categoryWeight1.setWeight(1);
                    categoryWeightMapper.insert(categoryWeight1);
                }else {
                    categoryWeight.setWeight(categoryWeight.getWeight()+1);
                    categoryWeightMapper.update(categoryWeight,wrapper);
                }
            }
        }
    }

    @Override
    public List<String> showByWeight() {
        return categoryWeightMapper.showByWeight();
    }

    @Override
    public void deleteById(Long id) {
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.eq("article_id",id);
        List<Category> categories =
                categoryMapper.selectList(categoryQueryWrapper);
        for (Category category : categories) {
            categoryMapper.deleteById(category);
            QueryWrapper<CategoryWeight> wrapper = new QueryWrapper<>();
            wrapper.eq("category",category.getCategoryName());
            CategoryWeight categoryWeight =
                    categoryWeightMapper.selectOne(wrapper);
            categoryWeight.setWeight(categoryWeight.getWeight()-1);
            categoryWeightMapper.update(categoryWeight,wrapper);
        }


    }
}
