package com.example.user.statemachinetrying;


public interface State {

    //сделан интерфейсом мз-за отсутствия множоественного наследования
    //(если бы был абстрактным классом ->)
    // какая то дефолтная реализация в теле метода

    void onEnter(Contract.View view);
    void onExit();

    //передача view предотвращает NPE
    void restoreState(Contract.View view);
}
