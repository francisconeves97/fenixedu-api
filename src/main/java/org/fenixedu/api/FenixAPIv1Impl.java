package org.fenixedu.api;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.ActiveStudentsGroup;
import org.fenixedu.academic.domain.accessControl.ActiveTeachersGroup;
import org.fenixedu.academic.domain.accessControl.AllAlumniGroup;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.api.beans.FenixPerson;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.dto.PersonInformationBean;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Path("/fenix/v1")
public class FenixAPIv1Impl extends FenixAPIv1 {

    @Override
    protected Set<FenixPerson.FenixRole> getPersonRoles(Person person, PersonInformationBean pib) {
        User user = person.getUser();

        final Set<FenixPerson.FenixRole> roles = new HashSet<>();

        if (new ActiveTeachersGroup().isMember(user)) {
            roles.add(new FenixPerson.TeacherFenixRole(pib.getTeacherDepartment()));
        }

        if (new ActiveStudentsGroup().isMember(user)) {
            roles.add(new FenixPerson.StudentFenixRole(pib.getStudentRegistrations()));
        }

        if (new AllAlumniGroup().isMember(user)) {
            ArrayList<Registration> concludedRegistrations = new ArrayList<>();
            if (person.getStudent() != null) {
                concludedRegistrations.addAll(person.getStudent().getConcludedRegistrations());
            }
            roles.add(new FenixPerson.AlumniFenixRole(concludedRegistrations));
        }

        return roles;
    }
}
