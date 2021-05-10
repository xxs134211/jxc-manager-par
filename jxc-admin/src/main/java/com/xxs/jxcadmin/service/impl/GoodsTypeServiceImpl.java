package com.xxs.jxcadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xxs.jxcadmin.dto.TreeDto;
import com.xxs.jxcadmin.pojo.GoodsType;
import com.xxs.jxcadmin.mapper.GoodsTypeMapper;
import com.xxs.jxcadmin.service.IGoodsTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxs.jxcadmin.utils.AssertUtil;
import com.xxs.jxcadmin.utils.PageResultUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品类别表 服务实现类
 * </p>
 *
 * @author xxs
 * @since 2021-05-08
 */
@Service
public class GoodsTypeServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsType> implements IGoodsTypeService {

    @Override
    public List<TreeDto> queryAllGoodsTypes(Integer typeId) {
        List<TreeDto> treeDtos = this.baseMapper.queryAllGoodsTypes();
        if(null != typeId){
            for (TreeDto treeDto : treeDtos) {
                if(treeDto.getId().equals(typeId)){
                    treeDto.setChecked(true);
                    break;
                }
            }
        }
        return treeDtos;
    }

    @Override
    public List<Integer> queryAllSubTypeIdsByTypeId(Integer typeId) {
        GoodsType goodsType = this.getById(typeId);
        if(goodsType.getPId()==-1){
            return  this.list().stream().map(GoodsType::getId).collect(Collectors.toList());
        }
        List<Integer> result = new ArrayList<>();
        result.add(typeId);
        return getSubTypeIds(typeId,result);
    }

    @Override
    public Map<String, Object> goodsTypeList() {
        List<GoodsType> goodsTypes = this.list();
        return PageResultUtil.getResult((long) goodsTypes.size(),goodsTypes);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveGoodsType(GoodsType goodsType) {
        /**
         * 设置父类别的state的值为1
         */
        AssertUtil.isTrue(StringUtils.isBlank(goodsType.getName()),"名称不能为空！");
        AssertUtil.isTrue(null == goodsType.getPId(),"请指定父类别！");
        goodsType.setState(0);
        AssertUtil.isTrue(!(this.save(goodsType)),"记录添加失败！");
        GoodsType parent = this.getById(goodsType.getPId());
        if(parent.getState()==0){
            parent.setState(1);
        }
        AssertUtil.isTrue(!(this.updateById(parent)),"记录添加失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteGoodsType(Integer id) {
        /**
         * 存在子类别不允许删除
         * 删除记录必须存在
         * 如果节点删除后，父节点没有子节点，设置上级节点state=0
         */
        GoodsType temp = this.getById(id);
        AssertUtil.isTrue(null == temp,"要删除的类别不存在！");
        int count = this.count(new QueryWrapper<GoodsType>().eq("p_id",id));
        AssertUtil.isTrue(count>0,"存在子类别，不能删除！");

        count = this.count(new QueryWrapper<GoodsType>().eq("p_id",temp.getPId()));
        if(count ==1){
            AssertUtil.isTrue(!(this.update(new UpdateWrapper<GoodsType>().set("state",0).eq("id",temp.getPId()))),"类别删除失败！");
        }
        AssertUtil.isTrue(!(this.removeById(id)),"删除失败！");
    }

    private List<Integer> getSubTypeIds(Integer typeId, List<Integer> result) {
        List<GoodsType> goodsTypes = this.baseMapper.selectList(new QueryWrapper<GoodsType>().eq("p_id",typeId));
        if(CollectionUtils.isNotEmpty(goodsTypes)){
            goodsTypes.forEach(goodsType -> {
                result.add(goodsType.getId());
                getSubTypeIds(goodsType.getId(),result);
            });
        }
        return result;
    }
}
