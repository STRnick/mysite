-- board

desc board;
desc user;

select b.no, b.title, u.name, b.hit, date_format(b.reg_date, '%Y-%m-%d %H:%h:%s') as reg_date
from board b, user u
where b.no = u.no;


