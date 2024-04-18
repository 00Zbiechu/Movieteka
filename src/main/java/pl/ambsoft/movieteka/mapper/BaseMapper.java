package pl.ambsoft.movieteka.mapper;

public interface BaseMapper<E, T> {

    E toEntity(T dto);

    T toDto(E entity);
}
