import type { CSSProperties, ExtractPropTypes } from 'vue';
import type { TourBtnProps, TourMask } from './types';
export declare const tourStepProps: {
    target: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | HTMLElement | (() => HTMLElement | null) | null) & {}) | (() => string | HTMLElement | (() => HTMLElement | null) | null) | ((new (...args: any[]) => (string | HTMLElement | (() => HTMLElement | null) | null) & {}) | (() => string | HTMLElement | (() => HTMLElement | null) | null))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    title: StringConstructor;
    description: StringConstructor;
    showClose: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, boolean, boolean>;
    closeIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component<any, any, any, import("vue").ComputedOptions, import("vue").MethodOptions>) & {}) | (() => string | import("vue").Component<any, any, any, import("vue").ComputedOptions, import("vue").MethodOptions>) | ((new (...args: any[]) => (string | import("vue").Component<any, any, any, import("vue").ComputedOptions, import("vue").MethodOptions>) & {}) | (() => string | import("vue").Component<any, any, any, import("vue").ComputedOptions, import("vue").MethodOptions>))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    showArrow: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, boolean, boolean>;
    placement: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => import("@floating-ui/core").Placement & {}) | (() => import("@floating-ui/core").Placement) | ((new (...args: any[]) => import("@floating-ui/core").Placement & {}) | (() => import("@floating-ui/core").Placement))[], "top" | "bottom" | "right" | "left" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end", unknown, string, boolean>;
    mask: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => TourMask & {}) | (() => TourMask) | ((new (...args: any[]) => TourMask & {}) | (() => TourMask))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    contentStyle: {
        readonly type: import("vue").PropType<CSSProperties>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    prevButtonProps: {
        readonly type: import("vue").PropType<TourBtnProps>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    nextButtonProps: {
        readonly type: import("vue").PropType<TourBtnProps>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    scrollIntoViewOptions: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (boolean | ScrollIntoViewOptions) & {}) | (() => boolean | ScrollIntoViewOptions) | ((new (...args: any[]) => (boolean | ScrollIntoViewOptions) & {}) | (() => boolean | ScrollIntoViewOptions))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    type: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => ("primary" | "defalut") & {}) | (() => "primary" | "defalut") | ((new (...args: any[]) => ("primary" | "defalut") & {}) | (() => "primary" | "defalut"))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
};
export declare type TourStepProps = ExtractPropTypes<typeof tourStepProps>;
export declare const tourStepEmits: {
    close: () => boolean;
};
export declare type TourStepEmits = typeof tourStepEmits;
