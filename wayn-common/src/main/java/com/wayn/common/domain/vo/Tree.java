package com.wayn.common.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 树形节点展示
 * @author wayn
 *
 * @param <T>
 */
@Data
public class Tree<T> implements Serializable {

	private static final long serialVersionUID = 1218560379804614405L;
	/**
	 * 节点ID
	 */
	private String id;
	/**
	 * 显示节点文本
	 */
	private String text;
	/**
	 * 节点状态，open closed
	 */
	private Map<String, Object> state;
	/**
	 * 节点是否被选中 true false
	 */
	private Boolean checked = false;
	/**
	 * 节点属性
	 */
	private Map<String, Object> attributes;

	/**
	 * 节点的子节点
	 */
	private List<Tree<T>> children = new ArrayList<>();

	/**
	 * 父ID
	 */
	private String parentId;
	/**
	 * 是否有父节点
	 */
	private Boolean hasParent = false;
	/**
	 * 是否有子节点
	 */
	private Boolean hasChildren = false;

}
