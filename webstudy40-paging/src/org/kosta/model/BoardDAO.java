package org.kosta.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

public class BoardDAO {
	private static BoardDAO dao=new BoardDAO();	
	private DataSource dataSource;
	private BoardDAO(){
		dataSource=DataSourceManager.getInstance().getDataSource();
	}
	public static BoardDAO getInstance(){
		return dao;
	}
	public Connection getConnection() throws SQLException{
		return dataSource.getConnection();
	}
	public void closeAll(PreparedStatement pstmt,Connection con) throws SQLException{
		if(pstmt!=null)
			pstmt.close();
		if(con!=null)
			con.close(); 
	}
	public void closeAll(ResultSet rs,PreparedStatement pstmt,Connection con) throws SQLException{
		if(rs!=null)
			rs.close();
		closeAll(pstmt,con);
	}	
	/**
	 * 페이지 번호에 해당하는 게시물 목록 리스트를 반환하는 메서드 
	 * LIST SQL -> Test 후 반영하세요 
	    SELECT b.no,b.title,b.hits,to_char(time_posted,'YYYY.MM.DD') as time_posted,m.id,m.name 
		FROM board b , board_member m
		WHERE b.id=m.id
		order by no desc
	 * @param pageNo
	 * @return
	 * @throws SQLException
	
	
	step : row_number()을 적용해 최근 게시물 5개만 조회
	
	select B.*, M.name
	from( 	select row_number() over( order by no desc) as rnum, no, title, hits, 	to_char(time_posted ,'yyyy-mm-dd') as time_posted , id	from board	)B, board_Member M
	where b.id = m.id and  rnum between 1and 5
	
	 */
	public ArrayList<PostVO> getPostingList(PagingBean pagingBean) throws SQLException{
		ArrayList<PostVO> list=new ArrayList<PostVO>();
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try{
			con=getConnection(); 
			StringBuilder sql=new StringBuilder();		
			sql.append("select i.no,i.title,i.hits,i.time_posted,m.id,m.name ");
	         sql.append("from(select row_number() over(order by no desc) as rnum, no, title,hits, to_char(time_posted,'YYYY.MM.DD') as time_posted,id from board) i, board_member m ");
	         sql.append("where i.id=m.id and rnum between ? and ? ");   
			//sql.append("order by no desc");
			pstmt=con.prepareStatement(sql.toString());			
			
			//start, endRowNumber 할당한다.
			pstmt.setInt(1, pagingBean.getStartRowNumber());
			pstmt.setInt(2, pagingBean.getEndRowNumber());
			
			
			rs=pstmt.executeQuery();	
			//목록에서 게시물 content는 필요없으므로 null로 setting
			//select no,title,time_posted,hits,id,name
			while(rs.next()){		
				PostVO pvo=new PostVO();
				pvo.setNo(rs.getString(1));
				pvo.setTitle(rs.getString(2));
				pvo.setHits(rs.getInt(3));
				pvo.setTimePosted(rs.getString(4));				
				MemberVO mvo=new MemberVO();
				mvo.setId(rs.getString(5));
				mvo.setName(rs.getString(6));
				pvo.setMemberVO(mvo);
				list.add(pvo);			
			}			
		}finally{
			closeAll(rs,pstmt,con);
		}
		return list;
	} 	
	 /**
     * Sequence 글번호로 게시물을 검색하는 메서드 
     * @param no
     * @return
     * @throws SQLException
     */
	public PostVO getPostingByNo(String no) throws SQLException{
		PostVO pvo=null;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try{
			con=getConnection();
			StringBuilder sql=new StringBuilder();
			sql.append("select b.title,to_char(b.time_posted,'YYYY.MM.DD  HH24:MI:SS') as time_posted");
			sql.append(",b.content,b.hits,b.id,m.name");
			sql.append(" from board b,board_member m");
			sql.append(" where b.id=m.id and b.no=?");		
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, no);
			rs=pstmt.executeQuery();		
			if(rs.next()){
				pvo=new PostVO();
				pvo.setNo(no);
				pvo.setTitle(rs.getString("title"));
				pvo.setContent(rs.getString("content"));				
				pvo.setHits(rs.getInt("hits"));
				pvo.setTimePosted(rs.getString("time_posted"));
				MemberVO mvo=new MemberVO();
				mvo.setId(rs.getString("id"));
				mvo.setName(rs.getString("name"));
				pvo.setMemberVO(mvo);
			}			
		}finally{
			closeAll(rs,pstmt,con);
		}
		return pvo;
	}
	/**
	 * 조회수 증가 
	 * @param no
	 * @throws SQLException
	 */
	public void updateHit(String no) throws SQLException{
		Connection con=null;
		PreparedStatement pstmt=null;
		try{
			con=getConnection(); 
			String sql="update board set hits=hits+1 where no=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, no);	
			pstmt.executeUpdate();			
		}finally{
			closeAll(pstmt,con);
		}
	}
	/**
	 * 게시물 등록 메서드  
	 * 게시물 등록 후 생성된 시퀀스를 BoardVO에 setting 한다. 
	 * insert into board(no,title,content,id,time_posted) values(board_seq.nextval,?,?,?,sysdate)
	 * @param vo
	 * @throws SQLException
	 */
	public void posting(PostVO vo) throws SQLException{
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try{
			con=getConnection();			
			StringBuilder sql=new StringBuilder();
			sql.append("insert into board(no,title,content,id,time_posted) ");
			sql.append("values(board_seq.nextval,?,?,?,sysdate)");			
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setString(3, vo.getMemberVO().getId());
			pstmt.executeUpdate();			
			pstmt.close();
			pstmt=con.prepareStatement("select board_seq.currval from dual");
			rs=pstmt.executeQuery();
			if(rs.next())
			vo.setNo(rs.getString(1));			
		}finally{
			closeAll(rs,pstmt,con);
		}
	}	
	/**
	 * 글번호에 해당하는 게시물을 삭제하는 메서드
	 * @param no
	 * @throws SQLException
	 */
	public void deletePosting(int no) throws SQLException{
		Connection con=null;
		PreparedStatement pstmt=null;
		try{
			con=getConnection(); 
			pstmt=con.prepareStatement("delete from board where no=?");
			pstmt.setInt(1, no);		
			pstmt.executeUpdate();			
		}finally{
			closeAll(pstmt,con);
		}
	}
	/**
	 * 게시물 정보 업데이트하는 메서드 
	 * @param vo
	 * @throws SQLException
	 */
	public void updatePosting(PostVO vo) throws SQLException{
		Connection con=null;
		PreparedStatement pstmt=null;
		try{
			con=getConnection();
			pstmt=con.prepareStatement("update board set title=?,content=? where no=?");
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setString(3, vo.getNo());	
			pstmt.executeUpdate();			
		}finally{
			closeAll(pstmt,con);
		}
	}
	
	public int getTotalPostCount() throws SQLException {
		int totalCount=0;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
		con=getConnection();
		String sql="select count(*) from board";
		pstmt=con.prepareStatement(sql);
		rs=pstmt.executeQuery();
		if(rs.next())
		totalCount=rs.getInt(1);
		}finally {
		closeAll(rs, pstmt, con);
		}
		return totalCount;
		}
	
	
}























