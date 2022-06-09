-- UserRepository

desc user;

insert 
 into user 
 values(null, '관리자', 'admin@mysite.com', '1234', 'male', now());
 
 select * from user;
 
 -- login
 select no, name
 from user
 where email='nicksumin97@gmail.com' 
 and password = '1234';
 
 -- findByNo
select no, name, email, gender 
from user
where no=2;
 