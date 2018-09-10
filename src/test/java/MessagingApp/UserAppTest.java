//package MessagingApp;
//
//import org.assertj.core.api.Assertions;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//
//import java.io.File;
//import java.io.IOException;
//import java.time.LocalDateTime;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.catchThrowable;
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//
//
//@RunWith(MockitoJUnitRunner.class)
//public class UserAppTest {
//
//
//    @Mock
//    IOUtils ioUtils;
//
//    UserApp userApp;
//
//    @Before
//    public void setUp() throws Exception {
//        userApp = new UserApp(ioUtils);
//    }
//
//    @Test
//    public void saveUser_ThrowsEmailAlreadyRegisteredException_IfTryToCreateWithSame() {
//        //given
//        String email = "email@email.com", name = "jane", password = "blabla123";
//        int age = 20;
//        //when
//        when(ioUtils.fileExists(any())).thenReturn(true);
//        Throwable result = catchThrowable(() -> userApp.saveUser());
//
//        //then
//        Assertions.assertThat(result)
//                .hasMessage("This email is already in use, try again\n");
//
//    }
//
//    @Test
//    public void saveUser_ReturnUser_IfSuccessAndEmailNotInUseAlready() throws IOException, EmailAlreadyRegisteredException {
//        //given
//        String email = "email@email.com", name = "jane", password = "blabla123";
//        int age = 20;
//        //when
//        when(ioUtils.fileExists(any())).thenReturn(false);
//        doNothing().when(ioUtils).writeUserToFile(eq(email), eq(name), eq(age), eq(password), any());
//        User result = userApp.saveUser();
//        //then
//        Assert.assertEquals(new User(email, name, age, password), result );
//
//    }
//    @Test
//    public void validateUser_throwsWrongEmailOrPasswordException_IfIncorrectEmailEntered(){
//        //given
//        String email = "bla@bla.com", password = "random";
//        //when
//        when(ioUtils.fileExists(any())).thenReturn(false);
//        Throwable result = catchThrowable(() -> userApp.validateUser(email, password));
//        //then
//        Assertions.assertThat(result)
//                .hasMessage("Your email or password is incorrect!");
//    }
//
//    @Test
//    public void validateUser_ReturnsTrue_IfEmailAndPasswordAreCorrect() throws WrongEmailOrPasswordException, IOException {
//        //given
//        String email = "blabla@blabla.com", password = "aaabbb2233";
//        File f = new File("C://Users//Sander//IdeaProjects//messaging-app//src//main//Users/" + email + ".txt");
//        //when
//        when(ioUtils.fileExists(f)).thenReturn(true);
//        boolean result = userApp.validateUser(email, password);
//        //then
//        assertThat(result).isTrue();
//    }
//}