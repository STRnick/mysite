-- board

desc board;
desc user;

select * from user;
select * from board;

delete from board;
ALTER TABLE board AUTO_INCREMENT=1;

select b.no, b.title, u.name, b.hit, date_format(b.reg_date, '%Y-%m-%d %H:%h:%s') as reg_date
from board b, user u
where b.user_no = u.no;

select b.g_no, b.title, b.contents
from board b, user u
where b.no = u.no;

select btitle, contents
from board, user
where g_no=1;

delete from board
where no=4;

insert into board values(null, '테스트', '테스트', 0, now(), 1, 2, 1, 1);
insert into board values(null, '테스트', '테스트', 0, now(), (select ifnull(max(g_no), 0)+1 from board b), 1, 1, 1);


