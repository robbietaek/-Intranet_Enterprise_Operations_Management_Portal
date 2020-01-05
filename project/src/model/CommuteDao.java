package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import model.mapper.CommuteMapper;

public class CommuteDao {
	private Map<String, Object> map = new HashMap<String, Object>();
	private Class<CommuteMapper> cls = CommuteMapper.class;
	
	public int maxnum() {
		SqlSession session = DBConnection.getConnection();
		try {
			return session.getMapper(cls).maxnum();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 DBConnection.close(session);
		}
		return 0;
	}
	
	public boolean insert(Commute commute) {
		SqlSession session = DBConnection.getConnection();
		try {
			int cnt = session.getMapper(cls).insert(commute);
			if (cnt > 0) return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 DBConnection.close(session);
		}
		return false;
	}
	public int commuteCount(String column, String find, String id) {
		SqlSession session = DBConnection.getConnection();
		try {
			map.clear();
			if(column != null) {
				String[] col = column.split(",");
			    map.put("col1",col[0]);
			    if(col.length == 2) {
			       map.put("col2",col[1]);
			    }
			    map.put("find",find);
			}
		    map.put("id", id);
			return session.getMapper(cls).commuteCount(map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 DBConnection.close(session);
		}
		return 0;
	}
	public List<Commute> list
	      (int pageNum,int limit,String column, String find, String id) {
		SqlSession session = DBConnection.getConnection();
		try {
			map.clear();
			map.put("start", (pageNum -1) * limit);
			map.put("limit",limit);
			if(column != null) {
				String[] col = column.split(",");
			    map.put("col1",col[0]);
			    if(col.length == 2) {
			       map.put("col2",col[1]);
			    }
			    map.put("find",find);
			    map.put("id", id);

			}
		    map.put("id", id);
			return session.getMapper(cls).select(map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 DBConnection.close(session);
		}
		return null;		
	}

	public Commute selectOne(String id) {
		SqlSession session = DBConnection.getConnection();
		try {
			map.clear();
			map.put("id",id);
			return session.getMapper(cls).select(map).get(0);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			 DBConnection.close(session);
		}
		return null;
	}
		
	
	//조회수 증가
	public void readcntadd(int num) {
		SqlSession session = DBConnection.getConnection();
		try {
			session.getMapper(cls).readcntadd(num);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			 DBConnection.close(session);
		}
	}
	//답글의 순서 지정
	public void grpStepAdd(int grp,int grpstep) {
		SqlSession session = DBConnection.getConnection();
		try {
			session.getMapper(cls).grpStepAdd(grp,grpstep);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 DBConnection.close(session);
		}
	}

}
