desc gallery;

delete from gallery;

select * from gallery;

ALTER TABLE gallery AUTO_INCREMENT=1;
SET @COUNT = 0;
UPDATE gallery SET no = @COUNT:=@COUNT+1;