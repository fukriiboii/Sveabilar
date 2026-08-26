package com.sveabilar.api.features.service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sveabilar.api.features.booking.entity.ServiceType;
import com.sveabilar.api.features.service.dto.ServiceOptionResponse;

@Service
public class ServiceCatalogService {

    public List<ServiceOptionResponse> getActiveServices() {
        return List.of(
                new ServiceOptionResponse(
                        ServiceType.TIRE_CHANGE,
                        ServiceType.TIRE_CHANGE.getDisplayName(),
                        "Vi byter dina däck direkt på plats hos dig, oavsett om du är hemma eller på jobbet. Vi ser samtidigt över däckens skick och kontrollerar att hjulen sitter korrekt. En smidig och trygg tjänst som sparar dig både tid och besväret med att åka till en verkstad.",
                        449,
                        60,
                        true,
                        false
                ),
                new ServiceOptionResponse(
                        ServiceType.HEADLIGHT_REPAIR,
                        ServiceType.HEADLIGHT_REPAIR.getDisplayName(),
                        "Vi återställer matta, gulnade och slitna strålkastare genom noggrann slipning och polering. Behandlingen förbättrar strålkastarens utseende och kan även bidra till bättre ljusgenomsläpp och sikt. Vi utför arbetet smidigt på plats hos dig.",
                        1199,
                        60,
                        true,
                        false
                ),
                new ServiceOptionResponse(
                        ServiceType.CAR_SERVICE,
                        ServiceType.CAR_SERVICE.getDisplayName(),
                        "Vi utför enklare service direkt på plats hos dig, med fokus på att hålla bilen i gott skick och säker på vägarna. Tjänsten kan bland annat omfatta oljebyte, kontroll och påfyllnad av vätskor samt byte av olje, luft och kupéfilter mm. Ett smidigt alternativ för dig som vill ta hand om bilens underhåll utan att behöva åka till verkstaden.",
                        null,
                        60,
                        true,
                        true
                ),

                new ServiceOptionResponse(
                        ServiceType.CAR_TRANSPORT,
                        ServiceType.CAR_TRANSPORT.getDisplayName(),
                        "Vi erbjuder smidiga och säkra biltransporter från A till B. Oavsett om bilen ska transporteras till en verkstad, kund, återförsäljare eller annan plats ser vi till att den kommer fram tryggt och på ett professionellt sätt. En enkel lösning när du behöver få bilen transporterad utan att behöva köra den själv.",
                        null,
                        60,
                        true,
                        true
                ),

                new ServiceOptionResponse(
                        ServiceType.MINOR_REPAIRS,
                        ServiceType.MINOR_REPAIRS.getDisplayName(),
                        "Vi utför mindre reparationer och underhåll direkt på plats hos dig. Det kan exempelvis vara byte av bromsbelägg, bromsskivor och andra enklare komponenter. Vi hjälper dig att hålla bilen säker och i gott skick, utan att du behöver lämna in den på verkstad.",
                        null,
                        60,
                        true,
                        true
                )

                

        );
    }
}
