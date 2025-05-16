insert into users(id,email,password,username)
   values (2, 'testAdmin@gmail.com', '$2a$10$VCYlJrpY40ooY7iD8Mfd/e.vlCvcd2zxWlvx1jX1Bz.pM6w5H53Je', 'testAdmin@gmail.com');
insert into user_role(user_id,role)
values (2,'ADMIN');