-- begin FB_STUDENT
alter table FB_STUDENT add constraint FK_FB_STUDENT_ON_GROUP foreign key (GROUP_ID) references FB_GROUP(ID)^
create index IDX_FB_STUDENT_ON_GROUP on FB_STUDENT (GROUP_ID)^
-- end FB_STUDENT
