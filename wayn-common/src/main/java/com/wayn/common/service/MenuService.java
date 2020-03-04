package com.wayn.common.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wayn.common.domain.Menu;
import com.wayn.common.domain.vo.MenuVO;
import com.wayn.common.domain.vo.Tree;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author wayn
 * @since 2019-04-13
 */
public interface MenuService extends IService<Menu> {
    boolean save(Menu menu);

    boolean update(Menu menu);

    boolean remove(Long id);

    public List<String> selectMenuIdsByUid(String id);

    public List<String> selectResourceByUid(String id);

    public List<Menu> selectTreeMenuByUserId(String id) throws Exception;

    public Tree<Menu> getTree();

    public Tree<Menu> getTree(String roleId);

    public List<Menu> list(MenuVO menu);
}