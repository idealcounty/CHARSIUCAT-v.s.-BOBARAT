/**
 * @description 扩展Vue-Router-Meta的类型，防止Typescript报错
 */

declare module 'vue-router' {
    interface RouteMeta {
        permission?: Array<string>
        title?: string
    }
}

export {}