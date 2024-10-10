package ru.job4j.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.dto.ListPagePostDto;
import ru.job4j.dto.OnePagePostDto;
import ru.job4j.model.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "created", target = "created")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(target = "price", expression = "java(getLatestPrice(post.getPriceHistory()))")
    @Mapping(source = "car.name", target = "carName")
    @Mapping(target = "firstFileId", expression = "java(mapFilesToIds(post.getFiles()))")
    @Mapping(source = "sold", target = "sold")
    ListPagePostDto getListPagePostDtoFromPost(Post post);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "car.name", target = "carName")
    @Mapping(target = "price", expression = "java(getLatestPrice(post.getPriceHistory()))")
    @Mapping(source = "car.engine.name", target = "engineName")
    @Mapping(source = "car.carModel.name", target = "carModelName")
    @Mapping(source = "description", target = "description")
    @Mapping(target = "ownerNames", expression = "java(mapCarOwners(post.getCar().getOwners()))")
    @Mapping(target = "participateNames", expression = "java(mapParticipateNames(post.getParticipates()))")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(target = "headFileId", expression = "java(mapFilesToIds(post.getFiles()))")
    @Mapping(target = "secondaryFileIds", expression = "java(secondaryFileIds(post.getFiles()))")
    @Mapping(source = "created", target = "created")
    @Mapping(source = "sold", target = "sold")
    OnePagePostDto getOnePagePostDtoFromPost(Post post);

    default BigInteger getLatestPrice(List<PriceHistory> priceHistory) {
        return priceHistory.get(priceHistory.size() - 1).getAfter();
    }

    default int mapFilesToIds(List<File> files) {
        if (files.isEmpty()) {
            return 0;
        }
        return files.get(0).getId();
    }

    default List<String> mapCarOwners(Set<Owner> owners) {
        return owners.stream()
                .map(Owner::getName)
                .toList();
    }

    default List<String> mapParticipateNames(List<User> participates) {
        return participates.stream()
                .map(User::getName)
                .toList();
    }

    default List<Integer> secondaryFileIds(List<File> files) {
        return files.stream()
                .map(File::getId)
                .skip(1)
                .toList();
    }
}
