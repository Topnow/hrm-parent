package cn.itsource.hrm.service.impl;

import cn.itsource.hrm.domain.CourseType;
import cn.itsource.hrm.mapper.CourseTypeMapper;
import cn.itsource.hrm.service.ICourseTypeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程目录 服务实现类
 * </p>
 *
 * @author TopOfTheWorld
 * @since 2019-12-25
 */
@Service
public class CourseTypeServiceImpl extends ServiceImpl<CourseTypeMapper, CourseType> implements ICourseTypeService {

    @Override
    public List<CourseType> listCourseType() {
        return loadTreeDataLoop_2();
    }

    /**
     * 查询所有父类
     * @return
     */
    @Override
    public List<CourseType> getAllParent() {
        //pid为 0的全是父类
        return baseMapper.selectList(
                new QueryWrapper<CourseType>().eq("pid", 0 ));
    }

    /**
     * 根据父 id 递归查询课程类型
     * @param pid
     * @return
     */
    public List<CourseType> getByParentId(Long pid){
        //先把所有的查询出来
        List<CourseType> children=baseMapper.selectList(
                new QueryWrapper<CourseType>().eq("pid", pid));

        //递归出口
        if (children==null || children.size()==0){
            return null;
        }
        //进入递归
        for (CourseType child : children) {
            List<CourseType> childs=getByParentId(child.getId());
            child.setChildren(childs);
        }
        return children;
    }

    /**
     * 循环方式
     * @return
     */
    public List<CourseType> loadTreeDataLoop_1(){
        //初始化集合放一级类型
        List<CourseType> firstLevelTypes=new ArrayList<>();
        //查询出所有类型  为null就是查询所有
        List<CourseType> courseTypes=baseMapper.selectList(null);

        //循环 依次判断
        for (CourseType courseType : courseTypes) {
            //如果时一级类型 直接放入
            if (courseType.getPid()==0L){
                firstLevelTypes.add(courseType);
            }else {
                //如果不是 就找父类型 并放入 children 中
                for (CourseType parent : courseTypes) {
                    if (courseType.getPid().longValue()==parent.getId()){
                        parent.getChildren().add(courseType);
                    }
                }
            }
        }
        return firstLevelTypes;
    }

    /**
     * 循环+map
     * @return
     */
    public List<CourseType> loadTreeDataLoop_2(){
        //初始化集合放一级类型
        List<CourseType> firstLevelTypes=new ArrayList<>();
        //查询出所有类型  为null就是查询所有
        List<CourseType> courseTypes=baseMapper.selectList(null);
        //创建 一个 map 并将数据放入到 map中，以 id为键 CourseType 为值
        Map<Long,CourseType> map=new HashMap<>();
        for (CourseType courseType : courseTypes) {
            map.put(courseType.getId(), courseType);
        }

        for (CourseType courseType : courseTypes) {
            if (courseType.getPid().longValue()==0L){
                firstLevelTypes.add(courseType);
            }else {
                CourseType parent = map.get(courseType.getPid());
                if (parent!=null){
                    parent.getChildren().add(courseType);
                }
            }
        }
        return firstLevelTypes;
    }
}
