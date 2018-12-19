package future;


/**
 * 先上一个场景：假如你突然想做饭，但是没有厨具，也没有食材。网上购买厨具比较方便，食材去超市买更放心。
 * <p>
 * 实现分析：在快递员送厨具的期间，我们肯定不会闲着，可以去超市买食材。所以，在主线程里面另起一个子线程去网购厨具。
 * <p>
 * 但是，子线程执行的结果是要返回厨具的，而run方法是没有返回值的。所以，这才是难点，需要好好考虑一下。
 */
public class CommonCook {


	public static void main(String[] args) throws InterruptedException {
		long startTime = System.currentTimeMillis();

		//第一步，网购厨具
		OnlineShopping shoppingThread = new OnlineShopping();
		shoppingThread.start();
		//等待厨具送到
		shoppingThread.join();

		//第二步 去超市买食材
		//模拟购买食材的时间
		Thread.sleep(2000);

		Shicai shicai = new Shicai();
		System.out.println("第二步：食材到位");

		//第三步 用厨具烹饪食材
		System.out.println("第三步：开始展现厨艺");
		cook(shoppingThread.chuju, shicai);


		System.out.println("总共用时: " + (System.currentTimeMillis() - startTime));
	}
//PS本例将join放在cook方法前可以节省2秒

	static void cook(Chuju chuju, Shicai shicai) {
	}

	static class OnlineShopping extends Thread {
		//厨具
		private Chuju chuju;

		@Override
		public void run() {
			System.out.println("第一步：下单");
			System.out.println("第一步：等待送货");
			try {
				//模拟送货时间
				Thread.sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("第一步：快递送到");
			chuju = new Chuju();
		}
	}


	// 厨具
	static class Chuju {
	}


	// 食材类
	static class Shicai {
	}

}
