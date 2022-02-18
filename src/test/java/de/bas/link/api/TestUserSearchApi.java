package de.bas.link.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bas.link.api.data.UserTestDataFactory;
import de.bas.link.domain.dto.ListResponse;
import de.bas.link.domain.dto.SearchRequest;
import de.bas.link.domain.dto.SearchUsersQuery;
import de.bas.link.domain.dto.UserView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static de.bas.link.util.JsonHelper.fromJson;
import static de.bas.link.util.JsonHelper.toJson;
import static java.lang.System.currentTimeMillis;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("ada.lovelace@nix.io")
public class TestUserSearchApi {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserTestDataFactory userTestDataFactory;

    @Autowired
    public TestUserSearchApi(MockMvc mockMvc, ObjectMapper objectMapper, UserTestDataFactory userTestDataFactory) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userTestDataFactory = userTestDataFactory;
    }

    @Test
    public void testSearch() throws Exception {
        UserView user1 = userTestDataFactory.createUser(String.format("william.baker.%d@gmail.com", currentTimeMillis()), "William Baker");
        UserView user2 = userTestDataFactory.createUser(String.format("james.adams.%d@gmail.com", currentTimeMillis()), "James Adams");
        UserView user3 = userTestDataFactory.createUser(String.format("evelin.clarke.%d@nix.io", currentTimeMillis()), "Evelyn Clarke");
        UserView user4 = userTestDataFactory.createUser(String.format("ella.davidson.%d@nix.io", currentTimeMillis()), "Ella Davidson");
        UserView user5 = userTestDataFactory.createUser(String.format("evelin.bradley.%d@outlook.com", currentTimeMillis()), "Evelyn Bradley");

        testIdFilter(user1.getId());
        testUsernameFilter();
        testFullNameFilter();

        userTestDataFactory.deleteUser(user1.getId());
        userTestDataFactory.deleteUser(user2.getId());
        userTestDataFactory.deleteUser(user3.getId());
        userTestDataFactory.deleteUser(user4.getId());
        userTestDataFactory.deleteUser(user5.getId());
    }

    private void testIdFilter(String id) throws Exception {
        SearchUsersQuery query;
        ListResponse<UserView> userViewList;

        // Search query with book id equal
        query = new SearchUsersQuery();
        query.setId(id);
        userViewList = execute("/api/v1/admin/user/search", query);
        assertEquals(1, userViewList.getItems().size(), "Invalid search result!");
    }

    private void testUsernameFilter() throws Exception {
        SearchUsersQuery query;
        ListResponse<UserView> userViewList;

        // Search query username starts with
        query = new SearchUsersQuery();
        query.setUsername("evelin");
        userViewList = execute("/api/v1/admin/user/search", query);
        assertEquals(2, userViewList.getItems().size(), "Invalid search result!");

        // Search query username contains
        query = new SearchUsersQuery();
        query.setUsername("gmail");
        userViewList = execute("/api/v1/admin/user/search", query);
        assertEquals(2, userViewList.getItems().size(), "Invalid search result!");

        // Search query username case insensitive
        query = new SearchUsersQuery();
        query.setUsername("William");
        userViewList = execute("/api/v1/admin/user/search", query);
        assertEquals(1, userViewList.getItems().size(), "Invalid search result!");
    }

    private void testFullNameFilter() throws Exception {
        SearchUsersQuery query;
        ListResponse<UserView> userViewList;

        // Search query full name starts with
        query = new SearchUsersQuery();
        query.setUsername("William");
        userViewList = execute("/api/v1/admin/user/search", query);
        assertEquals(1, userViewList.getItems().size(), "Invalid search result!");

        // Search query full name contains
        query = new SearchUsersQuery();
        query.setUsername("David");
        userViewList = execute("/api/v1/admin/user/search", query);
        assertEquals(1, userViewList.getItems().size(), "Invalid search result!");

        // Search query full name case insensitive
        query = new SearchUsersQuery();
        query.setUsername("CLARKE");
        userViewList = execute("/api/v1/admin/user/search", query);
        assertEquals(1, userViewList.getItems().size(), "Invalid search result!");
    }

    private ListResponse<UserView> execute(String url, SearchUsersQuery query) throws Exception {
        MvcResult result = this.mockMvc
                .perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(objectMapper, new SearchRequest<>(query))))
                .andExpect(status().isOk())
                .andReturn();

        return fromJson(objectMapper,
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
    }

}
