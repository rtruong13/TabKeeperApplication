package com.i.should.what.whatshouldi.DoPackage.Models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by ryan on 7/29/2015.
 */
public class DoDayModel {
    public Date date;
    public List<DoModel> watchList;
    public List<DoModel> listenList;

    public DoDayModel(List<DoModel> models) {
        if (models == null || models.size() == 0) return;
        date = models.get(0).dateToDo;
        watchList = new ArrayList<>();
        listenList = new ArrayList<>();

        for (DoModel m : models) {
            if (m.doType == DoModel.DoModelType.Watch) {
                watchList.add(m);
            } else {
                listenList.add(m);
            }
        }
    }

    public static List<DoDayModel> getAllDays(List<DoModel> allModels) {
        List<DoDayModel> daysModels = new ArrayList<>();

        if (allModels == null || allModels.size() == 0) return daysModels;

        Collections.sort(allModels, new Comparator<DoModel>() {
            @Override
            public int compare(DoModel lhs, DoModel rhs) {
                return -1*lhs.dateToDo.compareTo(rhs.dateToDo);
            }
        });

        List<DoModel> helper = new ArrayList<>();
        Calendar currentDate = new GregorianCalendar();
        Calendar calendar = new GregorianCalendar();

        currentDate.setTime(allModels.get(0).dateToDo);
        for (int i = 0; i < allModels.size(); i++) {
            DoModel model = allModels.get(i);
            calendar.setTime(model.dateToDo);
            if ((currentDate.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) &&
                    (currentDate.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) &&
                    (currentDate.get(Calendar.YEAR) == calendar.get(Calendar.YEAR))) {
                helper.add(model);
            } else {
                daysModels.add(new DoDayModel(helper));
                helper = new ArrayList<>();
                helper.add(model);
                currentDate.setTime(model.dateToDo);
            }
        }
        daysModels.add(new DoDayModel(helper));

        return daysModels;
    }
}
