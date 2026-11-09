package kh.edu.istad.iam.domain.usecase.user;

import kh.edu.istad.iam.domain.dto.query.UserFilterQuery;
import kh.edu.istad.iam.domain.dto.result.PageResult;
import kh.edu.istad.iam.domain.dto.result.UserSummary;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.mapper.UserDataMapper;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@RequiredArgsConstructor
public class ListUserUseCase {
    private final UserRepository userRepository;
    private final UserDataMapper userDataMapper;


    public PageResult<UserSummary> execute(UserFilterQuery query) {
        List<User> users = userRepository.findAll(query.page(), query.size(), query.sort());
        long total = userRepository.count();
        List<UserSummary> summaries = users.stream()
                .map(userDataMapper::userToUserSummary)
                .toList();
        return PageResult.of(summaries, query.page(), query.size(), total);
    }
}