package kh.edu.istad.iam.domain.usecase.role;

import kh.edu.istad.iam.domain.dto.query.RoleFilterQuery;
import kh.edu.istad.iam.domain.dto.result.PageResult;
import kh.edu.istad.iam.domain.dto.result.RoleSummary;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.mapper.RoleDataMapper;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@RequiredArgsConstructor

public class ListRoleUseCase {

    private final RoleRepository roleRepository;
    private final RoleDataMapper roleDataMapper;

    public PageResult<RoleSummary> execute(RoleFilterQuery query) {
        List<Role> roles = roleRepository.findAll(query.page(), query.size(), query.sort());
        long total = roleRepository.count();
        List<RoleSummary> summaries = roles.stream()
                .map(roleDataMapper::roleToRoleSummary)
                .toList();
        return PageResult.of(summaries, query.page(), query.size(), total);
    }
}
