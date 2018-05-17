package es.upsa.mimo.mimo18_androidarch.base

interface BasePresenter<in T : BaseView> {

    fun start(view: T)

    fun end()

}
