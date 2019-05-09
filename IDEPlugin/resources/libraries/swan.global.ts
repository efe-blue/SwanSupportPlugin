/**
 * onLaunch 参数
 */
class LaunchOptions {
    scene: string; // 打开智能小程序的场景值
    path: string; // 打开小程序的路径
    query: Object; // 打开小程序的query
    shareTicket: string; // 标记转发对象
    referrerInfo: {  // 当场景为由从另一个小程序打开时，返回此字段。
        appId: string, // 来源小程序的 appKey。
        extraData: Object // 来源小程序传过来的数据，scene=1037或1038时支持。
    };
}

/**
 * onShow 参数
 */
class ShowOptions {
    scene: string; // 打开智能小程序的场景值
    path: string; // 打开小程序的路径
    query: Object; // 打开小程序的query
    shareTicket: string; // 标记转发对象
    referrerInfo: {  // 当场景为由从另一个小程序打开时，返回此字段。
        appId: string, // 来源小程序的 appKey。
        extraData: Object // 来源小程序传过来的数据，scene=1037或1038时支持。
    };
    entryType: string; // 展现的来源标识，取值为 user/ schema /sys
    appURL: string; // 展现时的调起协议，仅当entryType值为 schema 时存在。
}

/**
 * App.js 对象
 * https://smartprogram.baidu.com/docs/develop/framework/app_service_register/#App/
 */
class Application {
    onLaunch(options: LaunchOptions): void {
    }

    onShow(options: ShowOptions): void {
    }

    onHide(): void {
    }

    onError(): void {
    }

    onPageNotFound(): void {
    }

    globalData?: Object;
}

/**
 * TabItem参数
 */
class TabItemOptions {
    index: number;
    pagePath: string;
    text: string;
}

/**
 * 分享内容参数
 */
class ShareOptions {
    title?: string;
    content?: string;
    imageUrl?: string;
    path?: string;
    success?: Function;
    fail?: Function;
    complete?: Function;
}

/**
 * Page 对象
 * https://smartprogram.baidu.com/docs/develop/framework/app_service_page/#Page/
 */
class PageClass {
    data?: Object;

    onLoad(options: Object): void {
    }

    onReady(): void {
    }

    onShow(): void {
    }

    onHide(): void {
    }

    onUnload(): void {
    }

    onForceReLaunch(): void {
    }

    onPullDownRefresh(): void {
    }

    onReachBottom(): void {
    }

    onShareAppMessage(res: Object): ShareOptions {
        return null;
    }

    onPageScroll(): void {
    }

    onTabItemTap(item: TabItemOptions): void {
    }
}

/**
 * 全局 getApp方法
 */
declare function getApp(): Application;

/**
 * 全局 getCurrentPages 方法
 */
declare function getCurrentPages(): PageClass[];

/**
 * 全局 Page 方法
 * @param page
 */
declare function Page(page: PageClass);

/**
 * 全局 App 方法
 * @param app
 */
declare function App(app: Application);