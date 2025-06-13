package com.project.bookommendbe.transfer;


import com.project.bookommendbe.account.User;
import com.project.bookommendbe.dto.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapstruct {

    UserVO toUserVo(User user);
    @Mapping(target = "id", ignore = true)
    User fromUserDTO(UserVO userVO);

}
