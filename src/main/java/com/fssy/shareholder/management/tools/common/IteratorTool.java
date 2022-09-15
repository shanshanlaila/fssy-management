/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2021-10-21            添加获取树形结构数据（向下递归）
 */
package com.fssy.shareholder.management.tools.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.fssy.shareholder.management.tools.exception.ServiceException;

@Component
/**
 * 迭代工具类
 * 
 * @author Solomon
 */
public class IteratorTool
{
	private static final Logger LOGGER = LoggerFactory.getLogger(IteratorTool.class);
	
	/**
	 * 获取树型结构（向上递归，目前有问题）
	 * 
	 * @param firstList       当轮遍历列表
	 * @param allList         所有数据列表
	 * @param result          树形结构的查找表
	 * @param key             保存子集属性名，如children
	 * @param parentFieldName 父关联字段名
	 * @return 结果
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getTreeList(
			List<Map<String, Object>> firstList,
			List<Map<String, Object>> allList, Map<String, Object> result,
			String key, String parentFieldName) throws Exception
	{
		// region
		List<Map<String, Object>> oriAllList = (List<Map<String, Object>>) deepCopt(
				allList);
		if (firstList.isEmpty())
		{
			return result;
		}
		// 父元素与当前遍历元素的对应关系map，结构为：{1：[1.1,1.2],2:[2.1],3}
		Map<String, Object> parentMap = new HashMap<>();
		// 遍历循环
		// 1）如果是没有父级元素的数据,parentId=0，放到结果中
		// 2）父元素与当前遍历元素的对应关系map
		for (Object data : firstList)
		{
			Map<String, Object> dataMap = (HashMap<String, Object>) data;
			// 获取父id
			String parentId = String.valueOf(dataMap.get(parentFieldName));
			// 1）判断是否存在父级，parentId=0，放到结果中
			if ("0".equals(parentId)) // 不存在父级，放到结果中
			{
				String id = String.valueOf(dataMap.get("id"));
				// 添加到对应key的map的子集合中
				@SuppressWarnings("unchecked")
				Map<String, Object> resultMap = (Map<String, Object>) result;
				if (resultMap.containsKey(id)) // 存在对应id的数据
				{
					// 获取对应数据
					Map<String, Object> resultTempMap = (Map<String, Object>) resultMap
							.get(id);
					List<Map<String, Object>> tempDataList = new ArrayList<Map<String, Object>>();
					if (resultTempMap.containsKey(key)) // 存在孩子集合，需要合并遍历数据的结果
					{
						// 取出结果的孩子集合
						tempDataList = (List<Map<String, Object>>) resultTempMap
								.get(key);
					}
					else // 不存在对应id的数据孩子集合，添加孩子属性到结果中，就是空
					{

					}
					// 取出遍历数据的孩子集合
					List<Map<String, Object>> dataMapList = (List<Map<String, Object>>) dataMap
							.get(key);
					// 合并两个集合
					// 这里合并存在两个一样的值就会出现重复 TODO
					tempDataList.addAll(dataMapList);
					// 根据sort正向排序集合
					tempDataList.sort(Comparator
							.comparing(IteratorTool::comparingBySort));
					// 替换合并结果
					resultTempMap.put(key, tempDataList);
					// 替换对应id的数据
					resultMap.put(id, resultTempMap);
				}
				else // 不存在结果添加到对应的子集key属性中
				{
					resultMap.put(id, dataMap);
				}
			}

			// 2）父元素与当前遍历元素的对应关系map，
			List<Object> parentNode = new ArrayList<>();
			// 下一轮遍历的数据，本轮的所有父节点，存在对应parentId的数据，把当前遍历数据添加到对应父数据的所有子集合中
			if (parentMap.containsKey(parentId))
			{
				Object tempObject = parentMap.get(parentId);
				if (tempObject instanceof List<?>)
				{
					// 获取parentId对应的父节点的所有子集合
					parentNode = (List<Object>) tempObject;
				}
			}
			else // 不存在对应parentId的数据，创建空的子集合，并把当前遍历数据添加到空集合中
			{

			}
			// 添加当前遍历数据到parentId对应的父节点的子集合中
			parentNode.add(dataMap);
			// 更新父元素与当前遍历元素的对应关系map
			parentMap.put(parentId, parentNode);
		}

		// 1）判断是否结束递归，退出条件为：当前遍历的所有数据父id在所有数据中都找不到
		// 2）不结束需要找到下一轮遍历的数据，并且添加数据子集合属性，为方法开始合并子集合数据提供支持
		// 用于下一轮的firstList的列表
		List<Map<String, Object>> nextList = new ArrayList<>();
		// 退出递归的标识符，true为需要退出，返回result
		boolean flag = true;
		// 遍历全部数据
		for (Object data : allList)
		{
			Map<String, Object> dataMap = (Map<String, Object>) data;
			// 遍历父元素与当前遍历元素的对应关系map
			for (Entry<String, Object> entry : parentMap.entrySet())
			{
				String parentIdString = entry.getKey();
				// 取出孩子集合
				List<Map<String, Object>> childrenDatasList = (List<Map<String, Object>>) entry
						.getValue();
				childrenDatasList.sort(
						Comparator.comparing(IteratorTool::comparingBySort));
				// 父元素id在所有数据中能找到
				if (String.valueOf(dataMap.get("id")).equals(parentIdString))
				{
					// 标识符改变
					flag = false;
					// 放到一轮循环数据的孩子集合属性中
					dataMap.put(key, childrenDatasList);
					// 添加到下一次遍历列表中
					nextList.add(dataMap);
					break;
				}
			}
		}

		// 标识符为true时退出递归
		if (flag)
		{
			return result;
		}

		// endregion
		return getTreeList(nextList, oriAllList, result, key, parentFieldName);
	}

	/**
	 * 按照sort排序
	 * 
	 * @param map 查找表
	 * @return
	 */
	private static Integer comparingBySort(Map<String, Object> map)
	{
		if (map.containsKey("sort"))
		{
			return (Integer) map.get("sort");
		}
		if (map.get("id") instanceof Long)
		{
			Long temp = (Long) map.get("id");
			return temp.intValue();
		}
		return (Integer) map.get("id");
	}

	/**
	 * 利用io对对象进行深度克隆
	 * 
	 * @param obj 需要克隆对象
	 * @return 克隆的另一个对象
	 * @throws Exception
	 */
	public static Object deepCopt(Object obj) throws Exception
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		// 将当前这个对象写到一个输出流当中，，因为这个对象的类实现了Serializable这个接口，所以在这个类中
		// 有一个引用，这个引用如果实现了序列化，那么这个也会写到这个输出流当中

		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bis);
		Object cloneObj = ois.readObject();
		bis.close();
		ois.close();
		return cloneObj;
		// 这个就是将流中的东西读出类，读到一个对象流当中，这样就可以返回这两个对象的东西，实现深克隆
	}

	/**
	 * 转换成列表
	 * 
	 * @param result 带层级的结果
	 * @return
	 */
	public static List<Map<String, Object>> getListFromMap(
			Map<String, Object> result)
	{
		// 从map中获取所有数据，形成列表
		List<Map<String, Object>> resultList = new ArrayList<>();
		if (ObjectUtils.isEmpty(result))
		{
			return resultList;
		}

		for (Object menu : result.values())
		{
			@SuppressWarnings("unchecked")
			Map<String, Object> data = (Map<String, Object>) menu;
			resultList.add(data);
		}
		return resultList;
	}

	/**
	 * 获取某个父节点下面的所有子节点
	 * 
	 * @param allList  所有元素
	 * @param pid      寻找的父主键
	 * @param children 所有子集
	 * @return
	 */
	public static List<Map<String, Object>> getChildrenListForParent(
			List<Map<String, Object>> allList, int pid,
			List<Map<String, Object>> children)
	{
		for (Map<String, Object> mu : allList)
		{
			// 遍历出父id等于参数的id，add进子节点集合
			if (Integer.valueOf(mu.get("parent").toString()) == pid)
			{
				// 递归遍历下一级
				getChildrenListForParent(allList,
						Integer.valueOf(mu.get("findId").toString()), children);
				children.add(mu);
			}
		}
		return children;
	}
	
	/**
	 * 把列表转换为树结构
	 *
	 * @param originalList      原始list数据
	 * @param idFieldName       作为唯一标示的字段名称
	 * @param pidFieldName      父节点标识字段名
	 * @param childrenFieldName 子节点（列表）标识字段名
	 * @return 树结构列表
	 */
	public static <T> List<T> list2TreeList(List<T> originalList,
			String idFieldName, String pidFieldName, String childrenFieldName)
	{
		// 获取根节点，即找出父节点为空的对象
		List<T> rootNodeList = new ArrayList<>();
		for (T t : originalList)
		{
			String parentId = null;
			try
			{
				if (t instanceof Map<?, ?>)
				{
					Map<?, ?> temp = (Map<?, ?>) t;
					parentId = String.valueOf(temp.get(pidFieldName));
				}
				else
				{
					parentId = BeanUtils.getProperty(t, pidFieldName);
				}
			}
			catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e)
			{
				e.printStackTrace();
			}
			if (ObjectUtils.isEmpty(parentId) || "0".equals(parentId))
			{
				rootNodeList.add(0, t);
			}
		}

		// 将根节点从原始list移除，减少下次处理数据
		originalList.removeAll(rootNodeList);

		// 递归封装树
		try
		{
			packTree(rootNodeList, originalList, idFieldName, pidFieldName,
					childrenFieldName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return rootNodeList;
	}

	/**
	 * 封装树（向下递归）
	 *
	 * @param parentNodeList    要封装为树的父节点对象集合
	 * @param originalList      原始list数据
	 * @param keyName           作为唯一标示的字段名称
	 * @param pidFieldName      父节点标识字段名
	 * @param childrenFieldName 子节点（列表）标识字段名
	 */
	private static <T> void packTree(List<T> parentNodeList,
			List<T> originalList, String keyName, String pidFieldName,
			String childrenFieldName) throws Exception
	{
		for (T parentNode : parentNodeList)
		{
			// 找到当前父节点的子节点列表
			List<T> children = packChildren(parentNode, originalList, keyName,
					pidFieldName, childrenFieldName);
			if (children.isEmpty())
			{
				continue;
			}

			// 将当前父节点的子节点从原始list移除，减少下次处理数据
			originalList.removeAll(children);

			// 开始下次递归
			packTree(children, originalList, keyName, pidFieldName,
					childrenFieldName);
		}
	}

	/**
	 * 封装子对象
	 *
	 * @param parentNode        父节点对象
	 * @param originalList      原始list数据
	 * @param keyName           作为唯一标示的字段名称
	 * @param pidFieldName      父节点标识字段名
	 * @param childrenFieldName 子节点（列表）标识字段名
	 */
	private static <T> List<T> packChildren(T parentNode, List<T> originalList,
			String keyName, String pidFieldName, String childrenFieldName)
			throws Exception
	{
		// 找到当前父节点下的子节点列表
		List<T> childNodeList = new ArrayList<>();
		String parentId;
		if (parentNode instanceof Map<?, ?>)
		{
			Map<?, ?> temp = (Map<?, ?>) parentNode;
			parentId = String.valueOf(temp.get(keyName));
		}
		else
		{
			parentId = BeanUtils.getProperty(parentNode, keyName);
		}
		for (T t : originalList)
		{
			String childNodeParentId;
			if (parentNode instanceof Map<?, ?>)
			{
				Map<?, ?> temp = (Map<?, ?>) t;
				childNodeParentId = String.valueOf(temp.get(pidFieldName));
			}
			else
			{
				childNodeParentId = BeanUtils.getProperty(t, pidFieldName);
			}
			if (parentId.equals(childNodeParentId))
			{
				childNodeList.add(t);
			}
		}
		// 将当前父节点下的子节点列表写入到当前父节点下（给子节点列表字段赋值）
		if (!childNodeList.isEmpty())
		{
			if (childNodeList instanceof List<?>)
			{
				childNodeList = (List<T>) childNodeList;
			}
			// 排序子结点
			childNodeList
					.sort(Comparator.comparing(IteratorTool::comparing));
			if (parentNode instanceof Map<?, ?>)
			{
				@SuppressWarnings("unchecked")
				Map<String, Object> temp = (Map<String, Object>) parentNode;
				temp.put(childrenFieldName, childNodeList);
			}
			else
			{
				FieldUtils.writeDeclaredField(parentNode, childrenFieldName,
						childNodeList, true);
			}
		}

		return childNodeList;
	}

	/**
	 * 按照sort，不存在按照id排序
	 * 
	 * @param T 范型
	 * @return
	 */
	private static <T> Integer comparing(T t)
	{
		if (t instanceof Map<?, ?>)
		{
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) t;
			if (map.containsKey("sort"))
			{
				return (Integer) map.get("sort");
			}
			if (map.get("id") instanceof Long)
			{
				Long temp = (Long) map.get("id");
				return temp.intValue();
			}
			return (Integer) map.get("id");
		}
		Integer result;
		try
		{
			result = Integer.valueOf(String
					.valueOf(MethodUtils.invokeMethod(t.getClass(), "getId")));
		}
		catch (NumberFormatException | NoSuchMethodException
				| IllegalAccessException | InvocationTargetException e)
		{
			e.printStackTrace();
			return 0;
		}
		return result;
	}
	
	/**
	 * php中的keyBy方法
	 * 
	 * @param key      取出的键
	 * @param dataList 数据列表
	 * @return
	 */
	public static Map<String, Map<String, Object>> keyBy(String key,
			List<Map<String, Object>> dataList)
	{
		Map<String, Map<String, Object>> result = new HashMap<>();
		if (ObjectUtils.isEmpty(dataList))
		{
			return result;
		}
		for (Map<String, Object> temp : dataList)
		{
			if (temp.containsKey(key))
			{
				result.put(String.valueOf(temp.get(key)), temp);
			}
		}
		return result;
	}
	
	/**
	 * php中的keyBy方法
	 * 
	 * @param key      取出的键
	 * @param dataList 数据列表
	 * @return
	 */
	public static <T> Map<String, T> keyByPattern(String key, List<T> dataList)
	{
		Map<String, T> result = new HashMap<>();
		if (ObjectUtils.isEmpty(dataList))
		{
			return result;
		}
		for (T t : dataList)
		{
			if (t instanceof Map<?, ?>) // map类型
			{
				@SuppressWarnings("unchecked")
				Map<String, Object> temp = (Map<String, Object>) t;
				if (temp.containsKey(key))
				{
					result.put(String.valueOf(temp.get(key)), t);
				}
			}
			else // 实体类型，用反射
			{
				try
				{
					result.put(BeanUtils.getProperty(t, key), t);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					LOGGER.error(e.toString());
					throw new ServiceException("系统错误，联系管理员");
				}
			}
		}
		return result;
	}

	/**
	 * php中的keys方法
	 * 
	 * @param key      取出的键
	 * @param dataList 数据列表
	 * @return
	 */
	public static List<String> keys(String key,
			List<Map<String, Object>> dataList)
	{
		List<String> keys = new ArrayList<>();
		if (ObjectUtils.isEmpty(dataList))
		{
			return keys;
		}
		for (Map<String, Object> temp : dataList)
		{
			if (temp.containsKey(key))
			{
				keys.add(String.valueOf(temp.get(key)));
			}
		}
		return keys;
	}
	
	public static <T> List<String> pluck(String key, List<T> dataList)
	{
		List<String> resultList = new ArrayList<>();
		if (ObjectUtils.isEmpty(dataList))
		{
			return resultList;
		}
		for (T t : dataList)
		{
			// 是map用get()
			if (t instanceof Map<?, ?>)
			{
				Map<?, ?> temp = (Map<?, ?>) t;
				resultList.add(String.valueOf(temp.get(key)));
			}
			// 是实体类用反射
			else
			{
				try
				{
					resultList.add(BeanUtils.getProperty(t, key));
				}
				catch (Exception e)
				{
					e.printStackTrace();
					LOGGER.error(e.toString());
					throw new ServiceException("系统错误，联系管理员");
				}
			}
		}
		return resultList;
	}
	
	/**
	 * 从子元素寻找其对应的父元素，并放到children属性中
	 * 
	 * @param allList 所有元素
	 * @param child   子元素
	 * @return Map 代有children的子元素
	 */
	public static Map<String, Object> getParentTreeList(List<Map<String, Object>> allList,
			Map<String, Object> child)
	{
		// 删除本次的child
		allList.remove(child);
		for (Map<String, Object> mu : allList)
		{
			List<Map<String, Object>> tempList = new ArrayList<>();
			// 本次的元素找到父元素时
			// 转换为int类型才能用双等号，否则需要使用equals
			if (Integer.valueOf(mu.get("id").toString()).intValue() == Integer
					.valueOf(child.get("parent").toString()).intValue())
			{
				// 父元素不为顶级元素
				if (Integer.valueOf(mu.get("parent").toString()) != 0)
				{
					// 从新找
					getParentTreeList(allList, mu);
				}
				// 无论是否为顶级元素都放到集合中，区别在于是顶级元素没有children属性
				tempList.add(mu);
				// 放到子集中
				child.put("children", tempList);
				return child;
			}
		}
		return child;
	}
}
