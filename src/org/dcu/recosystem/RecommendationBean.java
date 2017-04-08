package org.dcu.recosystem;

//import java.util.Date;

public class RecommendationBean implements Comparable<RecommendationBean> 
{
	
	private String rec_id;
	private Integer rec_rank;
	private Integer rec_amount;
	private String timestamp;
	
	public RecommendationBean(String rec_id, Integer rec_amountClicks, Integer rec_rank, String timestamp)
	{
		super();
		this.rec_id = rec_id;
		this.rec_rank = rec_rank;
		this.rec_amount = rec_amountClicks;
		this.timestamp = timestamp;
	}

	public String getRec_id()
	{
		return rec_id;
	}

	public void setRec_id(String rec_id) 
	{
		this.rec_id = rec_id;
	}

	public Integer getRec_rank() 
	{
		return rec_rank;
	}

	public void setRec_rank(Integer rec_rank)
	{
		this.rec_rank = rec_rank;
	}


	public Integer getRec_amount() 
	{
		return rec_amount;
	}

	public void setRec_amount(Integer rec_amount)
	{
		this.rec_amount = rec_amount;
	}

	public String getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}

	@Override
	public int compareTo(RecommendationBean rec)
	{
		return this.rec_rank.compareTo(rec.rec_rank);
	}
	


	
	
}

