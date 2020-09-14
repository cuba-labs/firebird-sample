package com.company.firebird4.web.screens.student;

import com.haulmont.cuba.gui.screen.*;
import com.company.firebird4.entity.Student;

@UiController("firebird4_Student.browse")
@UiDescriptor("student-browse.xml")
@LookupComponent("table")
@LoadDataBeforeShow
public class StudentBrowse extends MasterDetailScreen<Student> {
}