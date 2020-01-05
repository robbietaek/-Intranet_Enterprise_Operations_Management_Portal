package model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import model.Member;

public interface MemberMapper {
	@Select({"<script>",
		"select * from member ",
		"<if test='id != null'> where binary id=#{id}</if>",
		"</script>"})
	List<Member> select(Map<String, Object> map);
	
	@Select({"<script>",
		"select * from member where dept = '개발부서' and membertype = 1 ",
		"<if test='id != null'> and binary id=#{id} </if>",
		"</script>"})
	List<Member> select1(Map<String, Object> map);
	
	@Select({"<script>",
		"select * from member where dept = '회계부서'  and membertype = 1 ",
		"<if test='id != null'> and binary id=#{id}</if>",
		"</script>"})
	List<Member> select2(Map<String, Object> map);
	
	@Select({"<script>",
		"select * from member where dept = '금융부서'  and membertype = 1 ",
		"<if test='id != null'> and binary id=#{id}</if>",
		"</script>"})
	List<Member> select3(Map<String, Object> map);
	
	@Select({"<script>",
		"select * from member where dept = '영업부서' and membertype = 1 ",
		"<if test='id != null'> and binary id=#{id} </if>",
		"</script>"})
	List<Member> select4(Map<String, Object> map);
	
	@Select("select count(*) from member where dept = #{s} and membertype = 1")
	int count(String s);
	
	@Select({"<script>",
		"select * from member where membertype = 2",
		"<if test='id != null'> where binary id=#{id}</if>",
		"</script>"})
	List<Member> submitlist(Map<String, Object> map);
	
	@Select({"<script>",
		"select * from member ",
		"<if test='id != null'> where binary id=#{id}</if>",
		"</script>"})
	List<Member> deptselect(Map<String, Object> map);
	
	
	@Update("update member set membertype = 1 where id=#{id}")
	int submit(String id);
	
	@Delete("delete from member where id=#{id} and membertype = 2")
	int deny(String id);

	@Insert("insert into member "
	+ "(id, name, dept, position, birthday, hiredate, tel, pass, picture, membertype)"
	+ " values (#{id}, #{name}, #{dept}, #{position}, #{birthday}, #{hiredate}, #{tel}, #{pass}, #{picture}, #{membertype})")
	int insert(Member m);

	@Update("update member set pass=#{pass}, tel=#{tel}, picture=#{picture} where id=#{id}")
	int update(Member m);

	@Delete("delete from member where id=#{id}")
	int delete(String id);

	@Update("update member set pass=#{pass} where id=#{id}")
	int updatepass
	    (@Param("id") String id, @Param("pass") String pass);

	
    @Select("select count(*) from member "
  	+ " where id =#{id}  and tel = #{tel} and hiredate = #{hiredate} and membertype = 1")
	int pwSearch(@Param("id") String id, 
		@Param("tel") String tel,@Param("hiredate") String hiredate);
}
