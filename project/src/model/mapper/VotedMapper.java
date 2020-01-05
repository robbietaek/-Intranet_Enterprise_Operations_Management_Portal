package model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import model.Voted;

public interface VotedMapper {
	@Insert("insert into voted (id) values (#{id})")
	int insert(String id);
	
	
	@Select("SELECT * from voted")
	List<Voted> select(Map<String, Object> map);
	
	
	@Delete("delete from voted")
	int delete();
}
