package de.example.haegertime.timetables;

import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.users.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping(path="api/timeTableDays")
public class TimeTableController {

    private final TimeTableService ttService;

    public TimeTableController(TimeTableService ttService){this.ttService = ttService;}

    @GetMapping
    public List<TimeTableDay> getTimetable(){return ttService.getTimetable();}

    @GetMapping("/{id}")
    public TimeTableDay getTimetableDay(@PathVariable("id") Long id) throws InstanceNotFoundException {
        return ttService.getTimetableDay(id);
    }

    @GetMapping("/actualHours/{id}")
    public List<TimeTableDay> actualHourShow(@PathVariable("id") Long id,
                                             @RequestParam(required = false) String startDate,
                                             @RequestParam(required = false) String endDate)
    {
        return ttService.getTimeTableDayByEmployeeId(id);
    }

    @GetMapping("/hours/employees/{id}")
    public List<List<Double>> totalHoursEmployeeShow(@PathVariable("id") Long projectId) {
        return ttService.getTotalWorkingHoursOnAProjectGroupedByEmployeeId(projectId);
    }


    @PutMapping("/assignEmployee/{employeeId}/toDay/{dayId}")
    public void assignEmployeeToDay(@PathVariable Long dayId, @PathVariable Long employeeId) throws ItemNotFoundException {
        ttService.assignEmployeeToDay(dayId, employeeId);
    }

    @PutMapping("/assignProject/{projectId}/toDay/{dayId}")
    public void assignProjectToDay(@PathVariable Long projectId, @PathVariable Long dayId) throws ItemNotFoundException {
        ttService.assignProjectToDay(dayId, projectId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TimeTableDay registerNewTimeTable(@RequestBody TimeTableDay timeTableDay) {
        ttService.registerNewTimeTable(timeTableDay);
        return timeTableDay;
    }

    @PutMapping("/finalize/{dayId}")
    public void  finalizeTimeTableDay(@PathVariable Long dayId) throws ItemNotFoundException {
        ttService.finalizeTimeTableDay(dayId);
    }



    @GetMapping("/overhours/{id}")
    public double showOverTimeOfEmployeeById(@PathVariable("id") Long employeeId) {
        return ttService.showOverTimeOfEmployeeById(employeeId);
    }

    @PutMapping("/absence/holiday")
    public void changeAbsenceStatusToHoliday(@RequestParam Long dayId,@RequestParam Double duration) {
        ttService.changeAbsenceStatusToHoliday(dayId, duration);
    }


    @PutMapping("/absence/sick/employee/{id}")
    public void changeAbsenceStatusToSick(@PathVariable("id") Long employeeId,

                                            @RequestParam Long dayId,
                                            @RequestParam double duration) {
        ttService.changeAbsenceStatusToSick(employeeId, dayId, duration);
    }

    @GetMapping("/employeesOnHoliday")
    public Set<User> showAllEmployeesInHoliday(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date) {
        return ttService.showAllEmployeesOnHoliday(date);
    }

    @GetMapping("/sickEmployees")
    public Set<User> showAllSickEmployees(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date) {
        return ttService.showAllSickEmployees(date);
    }
}

