
select B.*, M.name
from(
select row_number() over( order by no desc) as rnum, no, title, hits,
to_char(time_posted ,'yyyy-mm-dd') as time_posted , id
from board
)
B, board_Member M
where b.id = m.id and  rnum between 1and 5