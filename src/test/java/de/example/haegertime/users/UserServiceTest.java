package de.example.haegertime.users;

import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.email.EmailService;
import de.example.haegertime.timetables.TimeTableRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private TimeTableRepository timeTableRepository;
    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, emailService, timeTableRepository);
    }


    @Test
    void shouldFindAllSortByRole() {
        //given
        List<User> users = new ArrayList<>();
        User user1 = new User("Johanna", "Hagelücken", "abcdefg", "jolu@gmx.net", Role.BOOKKEEPER);
        User user2 = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
        users.add(user1);
        users.add(user2);

        when(userRepository.findAll(Sort.by(Sort.Direction.ASC, "role"))).thenReturn(users);
        //when
        List<User> result = underTest.getAllUsers("role");
        //then
        assertThat(result.size()).isEqualTo(2);
        verify(userRepository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "role"));
    }

    @Test
    void shouldFindAllSortByLast() {
        //given
        List<User> users = new ArrayList<>();
        User user1 = new User("Johanna", "Hagelücken", "abcdefg", "jolu@gmx.net", Role.BOOKKEEPER);
        User user2 = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
        users.add(user1);
        users.add(user2);

        when(userRepository.findAll(Sort.by(Sort.Direction.ASC, "last"))).thenReturn(users);
        //when
        List<User> expected = underTest.getAllUsers("abc");


        //then
        assertThat(expected.size()).isEqualTo(2);
        verify(userRepository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "last"));
    }

    @Test
    void shouldFindAll() {
        //given -> Vorbereitung
        List<User> users = new ArrayList<>();
        User user1 = new User("Johanna", "Hagelücken", "abcdefg", "jolu@gmx.net", Role.BOOKKEEPER);
        User user2 = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
        users.add(user1);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        //when -> durchführung
        List<User> expected = underTest.getAllUsers(null);


        //then -> Ergebnis/checks
        assertThat(expected.size()).isEqualTo(2);
        verify(userRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("add an new User")
    void shouldCreateUser() {
        // given
        User user = new User("Anton", "Aus Tirol", "1234567", "anton.austirol@gmx.de", Role.EMPLOYEE);

        // when
        underTest.createUser(user);

        // then
        ArgumentCaptor<User> userArgumentCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository)
                .save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();
        assertEquals(capturedUser, user);
    }

    @Test
    @DisplayName("get User by id")
    void shouldFindUserById() {
        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
        doReturn(Optional.of(user)).when(userRepository).findById(1L);

        Optional<User> returnedUser = userRepository.findById(1L);

        Assertions.assertTrue(returnedUser.isPresent(), "User nicht gefunden");
        Assertions.assertSame(returnedUser.get(), user, "User gefunden");
    }


    @Test
    void shouldDeleteUserById() {
        //given
        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
        userRepository.save(user);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        //when
        underTest.deleteUser(1L);
        //then
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @Disabled
    void shouldNotDeleteUserById() {
        assertThatThrownBy(() -> underTest.deleteUser(10L))
                .isInstanceOf(ItemNotFoundException.class);
    }


    @Test
    @DisplayName("get User by UserName (Email)")
    @Disabled
    void shouldGetUserByUserName() {
        underTest.getByUsername("martin.stoller2@gmx.de");
        verify(userRepository).getUserByEmail("martin.stoller2@gmx.de");
    }


    @Test
    void shouldUpdateUserDetails() {
        User user = new User("Nick", "Petersen", "alarm1", "stoller.martin@gmx.de", Role.ADMIN);
        User loggedUser = new User("Nick", "Petersen", "alarm1", "stoller.martin@gmx.de", Role.ADMIN);
//        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.getUserByEmail(loggedUser.getEmail())).thenReturn(Optional.of(loggedUser));

        User expected = underTest.updateUserDetails(user, loggedUser);
        assertEquals(expected.getPassword(), user.getPassword());
        assertEquals(expected.getFirstname(), user.getFirstname());
        assertEquals(expected.getLastname(), user.getLastname());
        assertEquals(expected.getEmail(), loggedUser.getEmail());
        assertEquals(expected.getRole(), loggedUser.getRole());
        assertEquals(expected.getId(), loggedUser.getId());
//        assertEquals(expected.getFrozen(), loggedUser.isFrozen());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldUpdateUserName() {
        //given
        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        //when
        underTest.updateUserName(1l, "martin.stoller3@gmx.de");
        //then
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void itShouldDeactivateById() {
        //given
        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        //when
        underTest.deactivateUser(1L);
        //then
        verify(userRepository, times(1)).save(user);
    }


    @Test
    void shouldReactivateById() {
        //given
        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        //when
        underTest.reactivateUser(1L);
        //then
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldUpdateRole() {
        //given
        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        //when
        underTest.updateRole(1L, "ADMIN");
        //then
        verify(userRepository, times(1)).save(user);
    }


    @Test
    @Disabled
    void showMyRestHolidays() {
        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
        given(userRepository.getUserByEmail("martin.stoller2@gmx.de")).willReturn(Optional.of(user));

        underTest.showMyRestHolidays("martin.stoller2@gmx.de");

        verify(userRepository, times(1)).findAll();
    }


    @Test
    void declineForHoliday() {
        //given
        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        //when
        underTest.declineForHoliday(1L);
        //then
        verify(emailService, times(1)).send("martin.stoller2@gmx.de", "Decline for holidays", "Hi " + user.getFirstname()
                + ", your apply was cancelled");
    }
}