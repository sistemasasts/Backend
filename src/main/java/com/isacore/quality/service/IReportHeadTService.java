package com.isacore.quality.service;

import com.isacore.quality.model.ReportHeadT;
import com.isacore.util.CRUD;

public interface IReportHeadTService extends CRUD<ReportHeadT>{

	ReportHeadT findHeadByTypeReport (ReportHeadT rht);
}
