calculation.start()

calculation.feedback.waitTo beVisible()
calculation.results.should == [100, 230]