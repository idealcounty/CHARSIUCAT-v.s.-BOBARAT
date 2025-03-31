import type { CSSProperties, Component, ComputedRef, InjectionKey, Ref, SetupContext, VNode } from 'vue';
import type { UseNamespaceReturn } from 'element-plus/es/hooks';
import type { TourGap, TourMask } from './types';
import type { Placement, Strategy, VirtualElement } from '@floating-ui/dom';
export declare const useTarget: (target: Ref<string | HTMLElement | (() => HTMLElement | null) | null | undefined>, open: Ref<boolean>, gap: Ref<TourGap>, mergedMask: Ref<TourMask>, scrollIntoViewOptions: Ref<boolean | ScrollIntoViewOptions>) => {
    mergedPosInfo: ComputedRef<{
        left: number;
        top: number;
        width: number;
        height: number;
        radius: number;
    } | null>;
    triggerTarget: ComputedRef<HTMLElement | {
        getBoundingClientRect(): DOMRect;
    } | undefined>;
};
export interface TourContext {
    current: Ref<number>;
    total: ComputedRef<number>;
    showClose: Ref<boolean>;
    closeIcon: Ref<string | Component>;
    mergedType: Ref<'default' | 'primary' | undefined>;
    ns: UseNamespaceReturn;
    slots: SetupContext['slots'];
    updateModelValue(modelValue: boolean): void;
    onClose(): void;
    onFinish(): void;
    onChange(): void;
}
export declare const tourKey: InjectionKey<TourContext>;
export declare function isSameSteps(a: any[], b: any[]): boolean;
export declare const getNormalizedProps: (node: VNode, booleanKeys: string[]) => Record<string, any>;
export declare const useFloating: (referenceRef: Ref<HTMLElement | VirtualElement | null>, contentRef: Ref<HTMLElement | null>, arrowRef: Ref<HTMLElement | null>, placement: Ref<Placement | undefined>, strategy: Ref<Strategy>, offset: Ref<number>, zIndex: Ref<number>, showArrow: Ref<boolean>) => {
    update: () => Promise<void>;
    contentStyle: ComputedRef<CSSProperties>;
    arrowStyle: ComputedRef<CSSProperties>;
};
