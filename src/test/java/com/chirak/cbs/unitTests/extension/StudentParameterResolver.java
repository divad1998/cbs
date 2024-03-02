//package com.chirak.cbs.unitTests.extension;
//
//import com.chirak.cbs.entity.Student;
//import org.junit.jupiter.api.extension.*;
//
//public class StudentParameterResolver implements ParameterResolver {
//    public Student student;
//    public StudentParameterResolver() {
//        Student student1 = new Student();
//        student1.setFirstName("D");
//        student1.setMiddleName("D");
//        student1.setLastName("D");
//        student1.setSex("M");
//        student1.setAge(25);
//        student1.setStateOfOrigin("Abia");
//        student1.setLga("Ikwuano");
//        student1.setCity("abj");
//        student1.setResidentialAddress("blah");
//        student1.setPhoneNumber("123");
//        student1.setStudyCenter("abj");
//        student1.setEmail("d@gmail.com");
//        student1.setPassword("123456");
//        student1.setGuarantorFullName("DD");
//        student1.setGuarantorAge(58);
//        student1.setGuarantorSex("F");
//        student1.setGuarantorResidentialAddress("blah");
//        student1.setGuarantorPhoneNumber("1");
//        student1.setGuarantorEmail("mummy@gmail.com");
//
//        this.student = student1;
//    }
//    @Override
//    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
//        if (parameterContext.getParameter().getType().equals(Student.class)) {
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
//        return student;
//    }
//}
