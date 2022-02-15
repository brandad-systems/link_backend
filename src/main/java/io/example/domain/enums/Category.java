package io.example.domain.enums;

public interface Category {

    enum Electronics implements Category {
        Audio_HiFi,
        Foto_Kamera,
        Konsolen,
        PC_Notebook,
        Sonstiges
    }

    enum Sports implements Category {
        Camping,
        Kinder_Spielzeug,
        Sport_Equipment,
        Sonstiges
    }

    enum Household implements Category {
        Event_Zubehoer,
        Garten_Zubehoer,
        Küchen_Zubehoer,
        Werkzeug,
        Sonstiges
    }

};
