package de.bas.link.api.data;

import de.bas.link.domain.dto.CreateUserRequest;
import de.bas.link.domain.dto.UserView;
import de.bas.link.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Service
public class UserTestDataFactory {

    @Autowired
    private UserService userService;

    public UserView createUser(String username,
                               String fullName,
                               String password) {
        CreateUserRequest createRequest = new CreateUserRequest();
        createRequest.setUsername(username);
        createRequest.setFullName(fullName);
        createRequest.setPassword(password);
        createRequest.setRePassword(password);

        UserView userView = userService.create(createRequest);

        assertNotNull(userView.getId(), "User id must not be null!");
        assertEquals(fullName, userView.getFullName(), "User name update isn't applied!");

        return userView;
    }

    public UserView createUser(String username,
                               String fullName) {
        return createUser(username, fullName, "Test12345_");
    }

    public void deleteUser(String id) {
        userService.delete(new ObjectId(id));
    }

}
