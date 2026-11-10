package kh.edu.istad.iam.restapi.mapper;

import kh.edu.istad.iam.domain.dto.command.permission.CreatePermissionCommand;
import kh.edu.istad.iam.domain.dto.command.permission.UpdatePermissionCommand;
import kh.edu.istad.iam.domain.dto.result.PermissionResult;
import kh.edu.istad.iam.restapi.dto.request.CreatePermissionRequest;
import kh.edu.istad.iam.restapi.dto.request.UpdatePermissionRequest;
import kh.edu.istad.iam.restapi.dto.response.PermissionResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PermissionWebMapper {

    public CreatePermissionCommand toCommand(CreatePermissionRequest request) {
        return new CreatePermissionCommand(
                request.resource(),
                request.action(),
                request.description()
        );
    }

    public UpdatePermissionCommand toCommand(UUID uuid, UpdatePermissionRequest request) {
        return new UpdatePermissionCommand(
                uuid,
                request.description()
        );
    }

    public PermissionResponse toResponse(PermissionResult result) {
        return new PermissionResponse(
                result.uuid(),
                result.name(),
                result.resource(),
                result.action(),
                result.description(),
                result.isSystem(),
                result.isActive(),
                result.createdAt(),
                result.updatedAt()
        );
    }
}
