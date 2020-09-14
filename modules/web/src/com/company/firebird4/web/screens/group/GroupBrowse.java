package com.company.firebird4.web.screens.group;

import com.haulmont.cuba.gui.screen.*;
import com.company.firebird4.entity.Group;

@UiController("firebird4_Group.browse")
@UiDescriptor("group-browse.xml")
@LookupComponent("table")
@LoadDataBeforeShow
public class GroupBrowse extends MasterDetailScreen<Group> {
}