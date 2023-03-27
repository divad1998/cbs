package com.chirak.cbs.mapper;

import com.chirak.cbs.dto.AffiliateDto;
import com.chirak.cbs.dto.UpdatedAffiliateDto;
import com.chirak.cbs.entity.Affiliate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AffiliateMapper {
    AffiliateMapper instance = Mappers.getMapper(AffiliateMapper.class);

    Affiliate to_Affiliate(AffiliateDto dto);
    AffiliateDto to_AffiliateDto(Affiliate affiliate);
    Affiliate to_Affiliate(UpdatedAffiliateDto dto);
    UpdatedAffiliateDto to_UpdatedAffiliateDto(Affiliate affiliate);
}